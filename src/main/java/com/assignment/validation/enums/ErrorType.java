package com.assignment.validation.enums;


public enum ErrorType {

    INCORRECT_END_BALANCE("Incorrect End Balance"),
    DUPLICATE_REFERENCE("Duplicate Reference"),
    ;
    private final String message;

    private ErrorType(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
