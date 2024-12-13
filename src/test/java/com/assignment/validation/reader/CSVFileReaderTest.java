package com.assignment.validation.reader;

import com.assignment.validation.dto.Transaction;
import com.assignment.validation.exception.InvalidCSVContentException;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import static com.assignment.validation.utils.TestData.CSV_NO_ERRORS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CSVFileReaderTest {

    private final CSVFileReader csvFileReader = new CSVFileReader();

    @Test
    void testReadFile_validCSV() {

        InputStream inputStream = new ByteArrayInputStream(CSV_NO_ERRORS.getBytes());

        List<Transaction> transactions = csvFileReader.readFile(inputStream);

        assertEquals(2, transactions.size());
        assertEquals(194261, transactions.get(0).getReference());
        assertEquals("NL91RABO0315273637", transactions.get(0).getAccountNumber());
        assertEquals("Clothes from Jan Bakker", transactions.get(0).getDescription());
        assertEquals(21.6, transactions.get(0).getStartBalance());
        assertEquals(-41.83, transactions.get(0).getMutation());
        assertEquals(-20.23, transactions.get(0).getEndBalance());
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
