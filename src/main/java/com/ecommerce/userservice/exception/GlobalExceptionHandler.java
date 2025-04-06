package com.ecommerce.userservice.exception;

import com.ecommerce.userservice.dto.response.ApiErrorResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiErrorResponseDTO> apiExceptionHandler(ApiException e) {
        ApiErrorResponseDTO apiErrorResponseDTO = ApiErrorResponseDTO.builder()
                .message(e.getMessage())
                .build();

        return new ResponseEntity<>(apiErrorResponseDTO, e.getHttpStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponseDTO> methodArgumentNotValid(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        ApiErrorResponseDTO apiErrorResponseDTO = ApiErrorResponseDTO.builder()
                .errors(errors)
                .build();

        return new ResponseEntity<>(apiErrorResponseDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponseDTO> exceptionHandler(Exception e) {
        ApiErrorResponseDTO apiErrorResponseDTO = ApiErrorResponseDTO.builder()
                .message(e.getMessage())
                .build();

        return new ResponseEntity<>(apiErrorResponseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
