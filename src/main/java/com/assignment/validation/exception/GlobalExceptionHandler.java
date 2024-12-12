package com.assignment.validation.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidIBANException.class)
    public ErrorResponse handleInvalidIBANException(InvalidIBANException ex) {
        return new ErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(InvalidJSONContentException.class)
    public ErrorResponse handleInvalidJSONContentException(InvalidJSONContentException ex) {
        return new ErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(InvalidCSVContentException.class)
    public ErrorResponse handleInvalidCSVContentException(InvalidCSVContentException ex) {
        return new ErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(UnsupportedFileTypeException.class)
    public ErrorResponse handleUnsupportedFileTypeException(UnsupportedFileTypeException ex) {
        return new ErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(InvalidFileException.class)
    public ErrorResponse handleInvalidFileException(InvalidFileException ex) {
        return new ErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

}
