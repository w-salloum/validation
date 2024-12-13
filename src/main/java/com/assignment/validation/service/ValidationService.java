package com.assignment.validation.service;

import com.assignment.validation.dto.Report;
import com.assignment.validation.dto.Transaction;
import com.assignment.validation.dto.TransactionError;
import com.assignment.validation.enums.ErrorType;
import com.assignment.validation.exception.InvalidFileException;
import com.assignment.validation.exception.UnsupportedFileTypeException;
import com.assignment.validation.reader.CSVFileReader;
import com.assignment.validation.reader.FileReader;
import com.assignment.validation.reader.JSONFileReader;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

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
            errors.add(ErrorType.DUPLICATE_REFERENCE.getMessage());
        }
    }

    // Validates the end balance of a transaction
    private void validateEndBalance(Transaction transaction, List<String> errors) {
        double calculatedEndBalance = roundToTwoDecimalPlaces(transaction.getStartBalance() + transaction.getMutation());
        double actualEndBalance = roundToTwoDecimalPlaces(transaction.getEndBalance());

        if (Double.compare(calculatedEndBalance, actualEndBalance) != 0) {
            errors.add(ErrorType.INCORRECT_END_BALANCE.getMessage());
        }
    }

    // Rounds a value to 2 decimal places
    private double roundToTwoDecimalPlaces(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
