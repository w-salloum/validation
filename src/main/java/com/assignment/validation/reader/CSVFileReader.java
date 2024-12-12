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
import java.util.Arrays;
import java.util.List;

public class CSVFileReader implements FileReader {

    private static final List<String> EXPECTED_HEADERS = Arrays.asList(
            "Reference", "AccountNumber", "Description", "Start Balance", "Mutation", "End Balance"
    );

    @Override
    public List<Transaction> readFile(InputStream inputStream) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            CSVParser csvParser = CSVFormat.DEFAULT.parse(reader);

            // Validate Headers
            List<String> headers = csvParser.getHeaderNames();
            if (!headers.equals(EXPECTED_HEADERS)) {
                throw new InvalidCSVContentException("Invalid CSV headers: " + headers);
            }

            List<Transaction> result = new ArrayList<>();

            // Process Records
            for (CSVRecord record : csvParser) {
                String reference = record.get("Reference");
                String accountNumber = record.get("AccountNumber");
                String description = record.get("Description");
                double startBalance = Double.parseDouble(record.get("Start Balance"));
                double mutation = Double.parseDouble(record.get("Mutation"));
                double endBalance = Double.parseDouble(record.get("End Balance"));

                Transaction transaction = Transaction.builder()
                        .reference(Long.parseLong(reference))
                        .accountNumber(accountNumber)
                        .description(description)
                        .startBalance(startBalance)
                        .mutation(mutation)
                        .endBalance(endBalance)
                        .build();

                result.add(transaction);
            }
            return result;
        } catch (IOException e) {
            throw new InvalidCSVContentException("Error reading CSV file: "+ e.getMessage());
        }
    }
}
