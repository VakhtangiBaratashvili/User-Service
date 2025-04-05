package com.ecommerce.userservice.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AuthenticationRequestDTO {

    @NotNull(message = "This field is mandatory")
    @NotBlank(message = "This field is mandatory")
    @Email(message = "Please enter correct email")
    private String email;

    @NotNull(message = "This field is mandatory")
    @NotBlank(message = "This field is mandatory")
    private String password;
}
