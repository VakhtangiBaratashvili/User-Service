package com.ecommerce.userservice.controller;

import com.ecommerce.userservice.dto.request.ChangePasswordRequestDTO;
import com.ecommerce.userservice.dto.response.ApiSuccessResponseDTO;
import com.ecommerce.userservice.service.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/rest/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/change-password")
    public ResponseEntity<ApiSuccessResponseDTO> changePassword(@RequestBody @Valid ChangePasswordRequestDTO requestDTO,
                                                                Principal connectedUser) {
        return new ResponseEntity<>(userService.changePassword(requestDTO, connectedUser), HttpStatus.OK);
    }
}
