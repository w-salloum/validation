package com.assignment.validation.exception;

public class InvalidIBANException extends RuntimeException {
    public InvalidIBANException(String message) {
        super(message);
    }
}
