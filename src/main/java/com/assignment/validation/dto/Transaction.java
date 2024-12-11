package com.assignment.validation.dto;

import lombok.Value;

@Value
public class Transaction {
    private long reference;
    private String accountNumber;
    private String description;
    private double startBalance;
    private double mutation;
    private double endBalance;
}
