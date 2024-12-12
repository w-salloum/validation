package com.assignment.validation.service;

import com.assignment.validation.dto.Report;
import com.assignment.validation.exception.InvalidCSVContentException;
import com.assignment.validation.exception.InvalidFileException;
import com.assignment.validation.exception.UnsupportedFileTypeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.mock.web.MockMultipartFile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ValidationServiceTest {

    private ValidationService validationService;

    @BeforeEach
    void setUp() {
        validationService = new ValidationService();
    }

    @Test
    void validateFile_WhenFileIsNull_ShouldThrowInvalidFileException() {
        InvalidFileException exception = assertThrows(InvalidFileException.class, () -> validationService.validate(null));
        assertEquals("Invalid file. Please upload a file.", exception.getMessage());
    }

    @Test
    void validateFile_WhenFileIsEmpty_ShouldThrowInvalidFileException() {
        MockMultipartFile file = new MockMultipartFile("file", "test.csv", "text/csv", new byte[0]);
        InvalidFileException exception = assertThrows(InvalidFileException.class, () -> validationService.validate(file));
        assertEquals("Invalid file. Please upload a file.", exception.getMessage());
    }

    @Test
    void getFileReader_WhenFileIsUnsupported_ShouldThrowUnsupportedFileTypeException() {
        MockMultipartFile file = new MockMultipartFile("file", "test.xml", "text/xml", "test".getBytes());
        UnsupportedFileTypeException exception = assertThrows(UnsupportedFileTypeException.class, () -> validationService.validate(file));
        assertEquals("Unsupported file type: test.xml", exception.getMessage());
    }

    @Test
    void validate_WhenFileIsValidJSON_ShouldReturnReport() {
        String jsonContent = """
            [
                {
                    "reference": 1,
                    "accountNumber": "NL91RABO0315273637",
                    "description": "Book",
                    "startBalance": 100.0,
                    "mutation": 50.0,
                    "endBalance": 150.0
                },
                {
                    "reference": 2,
                    "accountNumber": "NL91RABO0315273638",
                    "description": "Toy",
                    "startBalance": 200.0,
                    "mutation": -50.0,
                    "endBalance": 150.0
                }
            ]
            """;

        MockMultipartFile file = new MockMultipartFile("file", "test.json", "application/json", jsonContent.getBytes());

        Report report = validationService.validate(file);

        assertEquals(2, report.getNumberOfValidTransactions());
        assertEquals(0, report.getNumberOfInvalidTransactions());
    }


    @Test
    void validate_WhenFileIsValidJSONWithErrors_ShouldReturnReportWithErrors() {
        String jsonContent = """
            [
                {
                    "reference": 1,
                    "accountNumber": "NL91RABO0315273637",
                    "description": "Book",
                    "startBalance": 100.0,
                    "mutation": 50.0,
                    "endBalance": 140.0
                },
                {
                    "reference": 1,
                    "accountNumber": "NL91RABO0315273637",
                    "description": "Duplicate",
                    "startBalance": 100.0,
                    "mutation": 50.0,
                    "endBalance": 150.0
                }
            ]
            """;

        MockMultipartFile file = new MockMultipartFile("file", "test.json", "application/json", jsonContent.getBytes());

        ValidationService spyService = Mockito.spy(validationService);

        Report report = spyService.validate(file);

        assertEquals(0, report.getNumberOfValidTransactions());
        assertEquals(2, report.getNumberOfInvalidTransactions());
        assertEquals(2, report.getTransactionErrors().size());
    }

    @Test
    void validate_WhenCSVFileIsValidWithErrors_ShouldReturnReportWithErrors() {
        String csvContent = """
                Reference,AccountNumber,Description,Start Balance,Mutation,End Balance
                1,NL91RABO0315273637,Book,100.0,50.0,140.0
                1,NL91RABO0315273637,Duplicate,100.0,50.0,150.0
                """;

        MockMultipartFile file = new MockMultipartFile("file", "test.csv", "text/csv", csvContent.getBytes());

        Report report = validationService.validate(file);

        assertEquals(0, report.getNumberOfValidTransactions());
        assertEquals(2, report.getNumberOfInvalidTransactions());
        assertEquals(2, report.getTransactionErrors().size());
    }

    @Test
    void validate_WhenCSVFileHasInvalidHeader_ShouldReturnException() {

        String csvContent = """
            Reference,AccountNumber,Description,Start Balance,Mutation,ERROR HEADER
            1,NL91RABO0315273637,Book,100.0,50.0,140.0
            1,NL91RABO0315273637,Duplicate,100.0,50.0,150.0
            """;

        MockMultipartFile file = new MockMultipartFile("file", "test.csv", "text/csv", csvContent.getBytes());

        assertThrows( InvalidCSVContentException.class , () -> validationService.validate(file));

    }

}
