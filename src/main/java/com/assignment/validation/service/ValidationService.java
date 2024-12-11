package com.assignment.validation.service;

import com.assignment.validation.dto.Report;
import com.assignment.validation.dto.Transaction;
import com.assignment.validation.dto.TransactionError;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ValidationService {

    public static Report validateTransactions(List<Transaction> transactions) {
        Set<Long> uniqueReferences = new HashSet<>();
        List<TransactionError> transactionErrors = new ArrayList<>();
        int validTransactionCount = 0;

        for (Transaction transaction : transactions) {
            List<String> errors = validateTransaction(transaction, uniqueReferences);

            if (errors.isEmpty()) {
                validTransactionCount++;
            } else {
                transactionErrors.add(new TransactionError(transaction.getReference(), errors));
            }
        }

        int invalidTransactionCount = transactionErrors.size();

        return new Report(validTransactionCount, invalidTransactionCount, transactionErrors);
    }

    // Helper method to validate a single transaction
    private static List<String> validateTransaction(Transaction transaction, Set<Long> uniqueReferences) {
        List<String> errors = new ArrayList<>();

        // Validate unique reference
        validateUniqueReference(transaction.getReference(), uniqueReferences, errors);

        // Validate end balance
        validateEndBalance(transaction, errors);

        return errors;
    }

    // Validates the uniqueness of a transaction reference
    private static void validateUniqueReference(Long reference, Set<Long> uniqueReferences, List<String> errors) {
        if (!uniqueReferences.add(reference)) {
            errors.add("Duplicate reference");
        }
    }

    // Validates the end balance of a transaction
    private static void validateEndBalance(Transaction transaction, List<String> errors) {
        double calculatedEndBalance = transaction.getStartBalance() + transaction.getMutation();
        if (Double.compare(calculatedEndBalance, transaction.getEndBalance()) != 0) {
            errors.add("Incorrect end balance");
        }
    }



    /*private void validateIBAN(String iban) {
        // 1. Check length
        if (iban == null || iban.length() < 5 || iban.length() > 34) {
            throw new InvalidIBANException("IBAN length must be between 5 and 34 characters");
        }

        // 2. Check character set
        iban = iban.toUpperCase();
        if (!iban.matches("[A-Z]{2}[0-9]{2}[A-Z0-9]{1,30}")) {
            throw new InvalidIBANException("IBAN contains invalid characters");
        }

        // 3. Modulo 97 check
        String reformattedIBAN = iban.substring(4) + iban.substring(0, 4);
        int total = 0;
        for (int i = 0; i < reformattedIBAN.length(); i++) {
            int charValue = Character.getNumericValue(reformattedIBAN.charAt(i));
            if (charValue < 0) { // Ensure valid numeric value
                throw new InvalidIBANException("IBAN is invalid");
            }
            total = (total * 10 + charValue) % 97;
        }
        if( total != 1) {
            throw new InvalidIBANException("IBAN is invalid");
        }
    }*/
   
}
