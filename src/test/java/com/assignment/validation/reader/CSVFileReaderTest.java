package com.assignment.validation.reader;

import com.assignment.validation.dto.Transaction;
import com.assignment.validation.exception.InvalidCSVContentException;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CSVFileReaderTest {

    private final CSVFileReader csvFileReader = new CSVFileReader();

    @Test
    void testReadFile_validCSV() {
        String csvContent = "Reference,AccountNumber,Description,Start Balance,Mutation,End Balance\n" +
                "12345,1234567890,Test Description,1000.0,200.0,1200.0\n" +
                "67890,9876543210,Another Description,1500.0,-300.0,1200.0";
        InputStream inputStream = new ByteArrayInputStream(csvContent.getBytes());

        List<Transaction> transactions = csvFileReader.readFile(inputStream);

        assertEquals(2, transactions.size());
        assertEquals(12345, transactions.get(0).getReference());
        assertEquals("1234567890", transactions.get(0).getAccountNumber());
        assertEquals("Test Description", transactions.get(0).getDescription());
        assertEquals(1000.0, transactions.get(0).getStartBalance());
        assertEquals(200.0, transactions.get(0).getMutation());
        assertEquals(1200.0, transactions.get(0).getEndBalance());
    }

    @Test
    void testReadFile_invalidCSV_missingHeaders() {
        String csvContent = "Reference,AccountNumber,Description,Start Balance,Mutation\n" +
                "12345,1234567890,Test Description,1000.0,200.0";
        InputStream inputStream = new ByteArrayInputStream(csvContent.getBytes());

        assertThrows(InvalidCSVContentException.class, () -> {
            csvFileReader.readFile(inputStream);
        }).getMessage().contains("CSV file has missing or invalid headers");

    }
}
