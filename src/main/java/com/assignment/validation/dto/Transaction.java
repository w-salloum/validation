package com.assignment.validation.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
public class Transaction {
    private long reference;
    private String accountNumber;
    private String description;
    private double startBalance;
    private double mutation;
    private double endBalance;

    @JsonCreator
    public Transaction(
            @JsonProperty("reference") long reference,
            @JsonProperty("accountNumber") String accountNumber,
            @JsonProperty("description") String description,
            @JsonProperty("startBalance") double startBalance,
            @JsonProperty("mutation") double mutation,
            @JsonProperty("endBalance") double endBalance) {
        this.reference = reference;
        this.accountNumber = accountNumber;
        this.description = description;
        this.startBalance = startBalance;
        this.mutation = mutation;
        this.endBalance = endBalance;
    }
}
