package com.assignment.validation.reader;

import com.assignment.validation.dto.Transaction;
import com.assignment.validation.exception.InvalidJSONContentException;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JSONFileReaderTest {

    private final JSONFileReader jsonFileReader = new JSONFileReader();

    @Test
    void testReadFile_validJSON() {
        // Prepare test data
        String jsonContent = "[{\"reference\":12345, \"accountNumber\":\"1234567890\", \"description\":\"Test Description\", \"startBalance\":1000.0, \"mutation\":200.0, \"endBalance\":1200.0}]";
        InputStream inputStream = new ByteArrayInputStream(jsonContent.getBytes());

        // Call the method
        List<Transaction> transactions = jsonFileReader.readFile(inputStream);

        // Verify the results
        assertNotNull(transactions);
        assertEquals(1, transactions.size());
        assertEquals(12345, transactions.get(0).getReference());
        assertEquals("1234567890", transactions.get(0).getAccountNumber());
        assertEquals("Test Description", transactions.get(0).getDescription());
        assertEquals(1000.0, transactions.get(0).getStartBalance());
        assertEquals(200.0, transactions.get(0).getMutation());
        assertEquals(1200.0, transactions.get(0).getEndBalance());
    }

    @Test
    void testReadFile_invalidJSON() {
        // Prepare test data (invalid JSON format)
        String jsonContent = "[{\"Invalid\":12345, \"accountNumber\":\"1234567890\", \"description\":\"Test Description\", \"startBalance\":1000.0, \"mutation\":200.0, \"endBalance\":1200.0]";
        InputStream inputStream = new ByteArrayInputStream(jsonContent.getBytes());


        // Call the method and verify that the exception is thrown
        InvalidJSONContentException thrown = assertThrows(InvalidJSONContentException.class, () -> {
            jsonFileReader.readFile(inputStream);
        });

        assertTrue(thrown.getMessage().contains("Invalid JSON content"));
    }
}
