package com.ecommerce.userservice.service.user;

import com.ecommerce.userservice.dto.request.ChangePasswordRequestDTO;
import com.ecommerce.userservice.dto.response.ApiSuccessResponseDTO;

import java.security.Principal;

public interface UserService {
    ApiSuccessResponseDTO changePassword(ChangePasswordRequestDTO requestDTO, Principal connectedUser);
}
