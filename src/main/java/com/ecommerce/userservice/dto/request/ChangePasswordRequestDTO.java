package com.ecommerce.userservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ChangePasswordRequestDTO {

    @NotNull(message = "This field is mandatory")
    @NotBlank(message = "This field is mandatory")
    private String password;

    @NotNull(message = "This field is mandatory")
    @NotBlank(message = "This field is mandatory")
    @Size(min = 8, message = "Password should be minimum of 8 characters long")
    private String newPassword;

    @NotNull(message = "This field is mandatory")
    @NotBlank(message = "This field is mandatory")
    @Size(min = 8, message = "Password should be minimum of 8 characters long")
    private String confirmPassword;

}
