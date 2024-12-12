package com.assignment.validation.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Transaction {
    private long reference;
    private String accountNumber;
    private String description;
    private double startBalance;
    private double mutation;
    private double endBalance;
}
