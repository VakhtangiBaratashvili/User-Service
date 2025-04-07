package com.ecommerce.userservice.service.auth;

import com.ecommerce.userservice.dto.request.AuthenticationRequestDTO;
import com.ecommerce.userservice.dto.request.NotificationRequestDTO;
import com.ecommerce.userservice.dto.request.RegistrationRequestDTO;
import com.ecommerce.userservice.dto.response.ApiSuccessResponseDTO;
import com.ecommerce.userservice.entity.Token;
import com.ecommerce.userservice.entity.User;
import com.ecommerce.userservice.enums.Role;
import com.ecommerce.userservice.exception.ApiException;
import com.ecommerce.userservice.repository.TokenRepository;
import com.ecommerce.userservice.repository.UserRepository;
import com.ecommerce.userservice.service.JwtService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;

    private final TokenRepository tokenRepository;

    private final PasswordEncoder passwordEncoder;

    private final ObjectMapper objectMapper;

    private final RestTemplate restTemplate;

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    @Value("${e-commerce.notification-service.url}")
    private String notificationServiceUrl;

    @Override
    public ApiSuccessResponseDTO register(RegistrationRequestDTO requestDTO) {
        if (userRepository.existsByEmail(requestDTO.getEmail())) {
            throw new ApiException("Email already in use", HttpStatus.BAD_REQUEST);
        }
        if (userRepository.existsByPhoneNumber(requestDTO.getPhoneNumber())) {
            throw new ApiException("Phone number already in use", HttpStatus.BAD_REQUEST);
        }

        User user = User.builder()
                .firstName(requestDTO.getFirstName())
                .lastName(requestDTO.getLastName())
                .role(Role.USER)
                .email(requestDTO.getEmail())
                .phoneNumber(requestDTO.getPhoneNumber())
                .password(passwordEncoder.encode(requestDTO.getPassword()))
                .apiKey(UUID.randomUUID().toString())
                .build();

        String activationCode = generateActivationCode();
        NotificationRequestDTO notificationRequestDTO = NotificationRequestDTO.builder()
                .receiver(user.getEmail())
                .subject("Email Verification")
                .message("Your verification code: " + activationCode)
                .build();

        ResponseEntity<Void> response = sendEmail(notificationRequestDTO);

        if (!response.getStatusCode().equals(HttpStatus.OK)) {
            throw new ApiException("Email not sent", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        userRepository.save(user);
        Token token = Token.builder()
                .token(activationCode)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(2))
                .user(user)
                .build();
        tokenRepository.save(token);

        return ApiSuccessResponseDTO.builder()
                .message("Verification code has been sent to your email")
                .build();
    }

    @Override
    public ApiSuccessResponseDTO login(AuthenticationRequestDTO requestDTO) {
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        requestDTO.getEmail(),
                        requestDTO.getPassword()
                )
        );
        Map<String, Object> claims = new HashMap<>();
        User user = (User) authenticate.getPrincipal();
        claims.put("fullName", user.fullName());
        String token = jwtService.generateToken(claims, user);
        return ApiSuccessResponseDTO.builder()
                .token(token)
                .build();
    }

    @Override
    public ApiSuccessResponseDTO activateAccount(String otp) {
        Token token = tokenRepository.findByToken(otp)
                .orElseThrow(
                        () ->  new ApiException("Token not found", HttpStatus.NOT_FOUND)
                );
        if (LocalDateTime.now().isAfter(token.getExpiresAt())) {
            sendValidationEmail(token.getUser());
            throw new ApiException("Token is expired. A new token has been sent to the same email", HttpStatus.BAD_REQUEST);
        }
        User user = userRepository.findById(token.getUser().getId())
                .orElseThrow(
                        () -> new ApiException("User not found", HttpStatus.NOT_FOUND)
                );
        user.setEnabled(true);
        userRepository.save(user);
        token.setValidatedAt(LocalDateTime.now());
        tokenRepository.save(token);

        return ApiSuccessResponseDTO.builder()
                .message("Account activated")
                .build();
    }

    private void sendValidationEmail(User user) {
        String newToken = generateAndSaveActivationToken(user);
        NotificationRequestDTO notificationRequestDTO = NotificationRequestDTO.builder()
                .receiver(user.getEmail())
                .subject("Email Verification")
                .message("Your verification code: " + newToken)
                .build();

        ResponseEntity<Void> voidResponseEntity = sendEmail(notificationRequestDTO);
        if (voidResponseEntity.getStatusCode().equals(HttpStatus.OK)) {
            log.info("Email sent successfully");
        } else {
            throw new ApiException("Email not sent", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private ResponseEntity<Void> sendEmail(NotificationRequestDTO requestDTO) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String json;
        try {
            json = objectMapper.writeValueAsString(requestDTO);
        } catch (JsonProcessingException e) {
            log.error("Error During Json Serializing: {}", e.getMessage());
            throw new ApiException("Error During Json Serializing: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        HttpEntity<String> entity = new HttpEntity<>(json, headers);

        try {
            return restTemplate.exchange(notificationServiceUrl + "send-email", HttpMethod.POST, entity, Void.class);
        } catch (Exception e) {
            log.error("Error during sending email: {}", e.getMessage());
            throw new ApiException("Error during sending email: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private String generateAndSaveActivationToken(User user) {
        String generatedToken = generateActivationCode();
        Token token = Token.builder()
                .token(generatedToken)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(2))
                .user(user)
                .build();
        tokenRepository.save(token);
        return generatedToken;
    }

    private String generateActivationCode() {
        String characters = "0123456789";
        StringBuilder activationCodeBuilder = new StringBuilder();
        SecureRandom secureRandom = new SecureRandom();
        for (int i = 0; i < 4; i++) {
            int randomIndex = secureRandom.nextInt(characters.length());
            activationCodeBuilder.append(characters.charAt(randomIndex));
        }
        return activationCodeBuilder.toString();
    }
}
