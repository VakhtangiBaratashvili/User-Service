package com.ecommerce.userservice.dto.request;

import com.ecommerce.userservice.validation.PhoneNumber;
import jakarta.validation.constraints.Email;
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
public class RegistrationRequestDTO {

    @NotNull(message = "This field is mandatory")
    @NotBlank(message = "This field is mandatory")
    private String firstName;

    @NotNull(message = "This field is mandatory")
    @NotBlank(message = "This field is mandatory")
    private String lastName;

    @NotNull(message = "This field is mandatory")
    @NotBlank(message = "This field is mandatory")
    @Email(message = "Please enter correct email")
    private String email;

    @NotNull(message = "This field is mandatory")
    @NotBlank(message = "This field is mandatory")
    @PhoneNumber(message = "Please enter correct phone number")
    private String phoneNumber;

    @NotNull(message = "This field is mandatory")
    @NotBlank(message = "This field is mandatory")
    @Size(min = 8, message = "Password should be minimum of 8 characters long")
    private String password;
}
