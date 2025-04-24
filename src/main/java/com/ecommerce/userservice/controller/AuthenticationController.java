package com.ecommerce.userservice.controller;

import com.ecommerce.userservice.dto.response.UserResponseDTO;
import com.ecommerce.userservice.service.auth.AuthenticationService;
import com.ecommerce.userservice.dto.response.ApiSuccessResponseDTO;
import com.ecommerce.userservice.dto.request.AuthenticationRequestDTO;
import com.ecommerce.userservice.dto.request.RegistrationRequestDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rest/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<ApiSuccessResponseDTO> register(@RequestBody @Valid RegistrationRequestDTO requestDTO) {
        return new ResponseEntity<>(authenticationService.register(requestDTO), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiSuccessResponseDTO> login(@RequestBody @Valid AuthenticationRequestDTO requestDTO) {
        return new ResponseEntity<>(authenticationService.login(requestDTO), HttpStatus.OK);
    }

    @GetMapping("/activate-account")
    public ResponseEntity<ApiSuccessResponseDTO> activateAccount(@RequestParam String otp) {
        return new ResponseEntity<>(authenticationService.activateAccount(otp), HttpStatus.OK);
    }

    @GetMapping("/user/{apiKey}")
    public ResponseEntity<UserResponseDTO> getUserByApiKey(@PathVariable String apiKey) {
        return new ResponseEntity<>(authenticationService.getUserByApiKey(apiKey), HttpStatus.OK);
    }
}
