package com.ecommerce.userservice.dto.response;

import com.ecommerce.userservice.enums.Role;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserResponseDTO {

    private Long id;

    private Role role;

    private Boolean accountLocked;

    private Boolean enabled;

}
