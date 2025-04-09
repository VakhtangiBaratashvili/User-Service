package com.ecommerce.userservice.service.user;

import com.ecommerce.userservice.dto.request.ChangePasswordRequestDTO;
import com.ecommerce.userservice.dto.response.ApiSuccessResponseDTO;
import com.ecommerce.userservice.entity.User;
import com.ecommerce.userservice.exception.ApiException;
import com.ecommerce.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {


    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public ApiSuccessResponseDTO changePassword(ChangePasswordRequestDTO requestDTO, Principal connectedUser) {
        User user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        if (!passwordEncoder.matches(requestDTO.getPassword(), user.getPassword())) {
            throw new ApiException("Incorrect current password", HttpStatus.BAD_REQUEST);
        }
        if (!requestDTO.getNewPassword().equals(requestDTO.getConfirmPassword())) {
            throw new ApiException("Passwords do not match", HttpStatus.BAD_REQUEST);
        }
        user.setPassword(passwordEncoder.encode(requestDTO.getNewPassword()));
        userRepository.save(user);

        return ApiSuccessResponseDTO.builder()
                .message("Password changed successfully")
                .build();
    }

}
