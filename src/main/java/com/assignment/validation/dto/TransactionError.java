package com.assignment.validation.dto;

import lombok.Value;

import java.util.List;

@Value
public class TransactionError {
    long reference;
    List<String> errors;
}
