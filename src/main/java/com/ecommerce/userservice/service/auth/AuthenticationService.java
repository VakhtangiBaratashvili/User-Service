package com.ecommerce.userservice.service.auth;

import com.ecommerce.userservice.dto.response.ApiSuccessResponseDTO;
import com.ecommerce.userservice.dto.request.AuthenticationRequestDTO;
import com.ecommerce.userservice.dto.request.RegistrationRequestDTO;
import com.ecommerce.userservice.dto.response.UserResponseDTO;

public interface AuthenticationService {
    ApiSuccessResponseDTO register(RegistrationRequestDTO requestDTO);
    ApiSuccessResponseDTO login(AuthenticationRequestDTO requestDTO);
    ApiSuccessResponseDTO activateAccount(String otp);
    UserResponseDTO getUserByApiKey(String apiKey);
}
