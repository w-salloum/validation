package com.assignment.validation.service;

import com.assignment.validation.dto.Report;
import com.assignment.validation.dto.TransactionError;
import com.assignment.validation.exception.InvalidFileException;
import com.assignment.validation.exception.UnsupportedFileTypeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.mock.web.MockMultipartFile;

import java.util.List;
import java.util.stream.Stream;

import static com.assignment.validation.enums.ErrorType.DUPLICATE_REFERENCE;
import static com.assignment.validation.enums.ErrorType.INCORRECT_END_BALANCE;
import static com.assignment.validation.utils.TestData.*;
import static org.junit.jupiter.api.Assertions.*;

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

    @ParameterizedTest
    @MethodSource("filesProvider")
    void validate_WhenValidFile_ShouldReturnReport(MockMultipartFile file, int expectedValid, int expectedInvalid, int expectedErrors, List<String> expectedErrorMessages) {

        Report report = validationService.validate(file);
        assertEquals(expectedValid, report.getNumberOfValidTransactions());
        assertEquals(expectedInvalid, report.getNumberOfInvalidTransactions());
        assertEquals(report.getTransactionErrors().size(), expectedErrors);
        // Validate transaction errors
        if (expectedErrors > 0) {
            List<TransactionError> transactionErrors = report.getTransactionErrors();
            for (int i = 0; i < transactionErrors.size(); i++) {
                TransactionError transactionError = transactionErrors.get(i);
                assertNotEquals(0, transactionError.getReference());
                assertNotNull(transactionError.getErrors());
                assertTrue(transactionError.getErrors().contains(expectedErrorMessages.get(i)));
            }
        }
    }

    private static Stream<Arguments> filesProvider() {
        return Stream.of(
                Arguments.of(new MockMultipartFile("file", "test.json", "application/json", JSON_NO_ERRORS.getBytes()), 2, 0, 0, List.of()),
                Arguments.of(new MockMultipartFile("file", "test.csv", "text/csv", CSV_NO_ERRORS.getBytes()), 2, 0, 0, List.of()),

                Arguments.of(new MockMultipartFile("file", "test.json", "application/json", JSON_DUPLICATE_REFERENCE.getBytes()), 1, 1, 1, List.of(DUPLICATE_REFERENCE.getMessage())),
                Arguments.of(new MockMultipartFile("file", "test.csv", "text/csv", CSV_DUPLICATE_REFERENCE.getBytes()), 1, 1, 1, List.of(DUPLICATE_REFERENCE.getMessage())),

                Arguments.of(new MockMultipartFile("file", "test.json", "application/json", JSON_INCORRECT_END_BALANCE.getBytes()), 1, 1, 1, List.of(INCORRECT_END_BALANCE.getMessage())),
                Arguments.of(new MockMultipartFile("file", "test.csv", "text/csv", CSV_INCORRECT_END_BALANCE.getBytes()), 1, 1, 1, List.of(INCORRECT_END_BALANCE.getMessage()))
        );
    }

}
