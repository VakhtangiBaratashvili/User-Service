package com.ecommerce.userservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class NotificationRequestDTO {

    private String receiver;

    private String subject;

    private String message;
}
