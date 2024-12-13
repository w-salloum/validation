package com.assignment.validation.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidIBANException.class)
    public ResponseEntity<ErrorResponse> handleInvalidIBANException(InvalidIBANException ex) {
        return new ResponseEntity<>(new ErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidJSONContentException.class)
    public ResponseEntity<ErrorResponse> handleInvalidJSONContentException(InvalidJSONContentException ex) {
        return new ResponseEntity<>(new ErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidCSVContentException.class)
    public ResponseEntity<ErrorResponse> handleInvalidCSVContentException(InvalidCSVContentException ex) {
        return new ResponseEntity<>(new ErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnsupportedFileTypeException.class)
    public ResponseEntity<ErrorResponse> handleUnsupportedFileTypeException(UnsupportedFileTypeException ex) {
        return new ResponseEntity<>(new ErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidFileException.class)
    public ResponseEntity<ErrorResponse> handleInvalidFileException(InvalidFileException ex) {
        return new ResponseEntity<>(new ErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

}
