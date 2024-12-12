package com.assignment.validation.service;

import com.assignment.validation.dto.Report;
import com.assignment.validation.dto.Transaction;
import com.assignment.validation.dto.TransactionError;
import com.assignment.validation.exception.InvalidFileException;
import com.assignment.validation.exception.UnsupportedFileTypeException;
import com.assignment.validation.reader.CSVFileReader;
import com.assignment.validation.reader.FileReader;
import com.assignment.validation.reader.JSONFileReader;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ValidationService {

    private static final String UNSUPPORTED_FILE_TYPE_MSG = "Unsupported file type: ";
    private static final String INVALID_FILE_MSG = "Invalid file. Please upload a file.";

    public Report validate(MultipartFile file) {
        validateFile(file);

        try (var inputStream = file.getInputStream()) {
            // get the appropriate file reader based on the file type
            // then start processing the file
            FileReader fileReader = getFileReader(file.getOriginalFilename());
            List<Transaction> transactions = fileReader.readFile(inputStream);
            return validateTransactions(transactions);
        } catch (IOException e) {
            throw new InvalidFileException("Error reading file: " + e.getMessage());
        }
    }

    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new InvalidFileException(INVALID_FILE_MSG);
        }
    }

    private FileReader getFileReader(String filename) {
        if (filename.endsWith(".csv")) {
            return new CSVFileReader();
        }
        if (filename.endsWith(".json")) {
            return new JSONFileReader();
        }
        throw new UnsupportedFileTypeException(UNSUPPORTED_FILE_TYPE_MSG + filename);
    }

    private Report validateTransactions(List<Transaction> transactions) {
        Set<Long> uniqueReferences = new HashSet<>();

        List<TransactionError> transactionErrors = transactions.stream()
                .map(transaction -> validateTransaction(transaction, uniqueReferences))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();

        int validTransactionCount = transactions.size() - transactionErrors.size();
        return new Report(validTransactionCount, transactionErrors.size(), transactionErrors);
    }

    // Helper method to validate a single transaction
    private Optional<TransactionError> validateTransaction(Transaction transaction, Set<Long> uniqueReferences) {
        List<String> errors = new ArrayList<>();

        validateUniqueReference(transaction.getReference(), uniqueReferences, errors);
        validateEndBalance(transaction, errors);

        return errors.isEmpty() ? Optional.empty() : Optional.of(new TransactionError(transaction.getReference(), errors));
    }

    // Validates the uniqueness of a transaction reference
    private void validateUniqueReference(Long reference, Set<Long> uniqueReferences, List<String> errors) {
        if (!uniqueReferences.add(reference)) {
            errors.add("Duplicate reference");
        }
    }

    // Validates the end balance of a transaction
    private void validateEndBalance(Transaction transaction, List<String> errors) {
        double calculatedEndBalance = transaction.getStartBalance() + transaction.getMutation();
        if (Double.compare(calculatedEndBalance, transaction.getEndBalance()) != 0) {
            errors.add("Incorrect end balance");
        }
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
