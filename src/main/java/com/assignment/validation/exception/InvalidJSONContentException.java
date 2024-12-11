package com.assignment.validation.exception;

public class InvalidJSONContentException extends RuntimeException {
    public InvalidJSONContentException(String message) {
        super(message);
    }
}
