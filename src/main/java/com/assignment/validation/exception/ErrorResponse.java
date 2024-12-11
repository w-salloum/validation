package com.assignment.validation.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Data
public class ErrorResponse {
    private HttpStatus status;
    private String message;
}
