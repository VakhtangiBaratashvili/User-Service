package com.ecommerce.userservice.service.auth;

import com.ecommerce.userservice.dto.response.ApiSuccessResponseDTO;
import com.ecommerce.userservice.dto.request.AuthenticationRequestDTO;
import com.ecommerce.userservice.dto.request.RegistrationRequestDTO;

public interface AuthenticationService {
    ApiSuccessResponseDTO register(RegistrationRequestDTO requestDTO);
    ApiSuccessResponseDTO login(AuthenticationRequestDTO requestDTO);
    ApiSuccessResponseDTO activateAccount(String otp);
}
