package com.assignment.validation.dto;

import lombok.Value;

import java.util.List;

@Value
public class Report {
    int numberOfValidTransactions;
    int numberOfInvalidTransactions;
    List<TransactionError> transactionErrors;
}
