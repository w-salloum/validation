package com.assignment.validation.reader;

import com.assignment.validation.dto.Transaction;
import com.assignment.validation.exception.InvalidCSVContentException;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CSVFileReader implements FileReader {

    private static final List<String> EXPECTED_HEADERS = List.of("Reference", "AccountNumber", "Description", "Start Balance", "Mutation", "End Balance");

    @Override
    public List<Transaction> readFile(InputStream inputStream) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            CSVParser csvParser = CSVFormat.DEFAULT
                    .builder()
                    .setHeader()
                    .setSkipHeaderRecord(true)
                    .build()
                    .parse(reader);

            // Validate Headers
            validateHeaders(csvParser.getHeaderNames());

            List<Transaction> transactions = new ArrayList<>();

            // Process Records
            for (CSVRecord record : csvParser) {
                transactions.add(parseTransaction(record));
            }

            return transactions;
        } catch (IOException | IllegalArgumentException e) {
            throw new InvalidCSVContentException("Error reading CSV file: " + e.getMessage());
        }
    }

    private void validateHeaders(List<String> headers) {
        if (!headers.containsAll(EXPECTED_HEADERS)) {
            throw new InvalidCSVContentException("CSV file has missing or invalid headers. Expected: " + EXPECTED_HEADERS + ", Found: " + headers);
        }
    }

    private Transaction parseTransaction(CSVRecord record) {
        try {
            return Transaction.builder()
                    .reference(Long.parseLong(record.get("Reference")))
                    .accountNumber(record.get("AccountNumber"))
                    .description(record.get("Description"))
                    .startBalance(Double.parseDouble(record.get("Start Balance")))
                    .mutation(Double.parseDouble(record.get("Mutation")))
                    .endBalance(Double.parseDouble(record.get("End Balance")))
                    .build();
        } catch (NumberFormatException e) {
            throw new InvalidCSVContentException("Invalid number format in record: " + record.toString());
        }
    }
}
