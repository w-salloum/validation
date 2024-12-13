package com.assignment.validation.api;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.stream.Stream;

import static com.assignment.validation.enums.ErrorType.DUPLICATE_REFERENCE;
import static com.assignment.validation.enums.ErrorType.INCORRECT_END_BALANCE;
import static com.assignment.validation.utils.TestData.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ValidationControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @ParameterizedTest
    @MethodSource("filesProvider")
    void validate_WhenFileIsPassed_ShouldReturnCorrectReport(MockMultipartFile file, int expectedValidCount, int expectedInvalidCount, int expectedErrors,List<String> expectedErrorMessages) throws Exception {
        // Perform the API request with the provided file and store the result
        var result = mockMvc.perform(multipart("/api/validate").file(file))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numberOfValidTransactions").value(expectedValidCount))
                .andExpect(jsonPath("$.numberOfInvalidTransactions").value(expectedInvalidCount))
                .andExpect(jsonPath("$.transactionErrors.size()").value(expectedErrors));

        // If there are invalid transactions, check the error messages
        if (expectedInvalidCount > 0) {
            for (int i = 0; i < expectedErrorMessages.size(); i++) {
                result.andExpect(jsonPath("$.transactionErrors[" + i + "].errors.size()").value(1))
                        .andExpect(jsonPath("$.transactionErrors[" + i + "].errors[0]").value(expectedErrorMessages.get(i)))
                        .andExpect(jsonPath("$.transactionErrors[" + i + "].reference").isNumber());
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

    @Test
    void validate_WhenFileIsInvalidCSVFormat_ShouldReturnBadRequest() throws Exception {
        MockMultipartFile invalidFile = new MockMultipartFile("file", "invalid.json","application/json", JSON_INVALID_CONTENT.getBytes());
        // Perform the API request and check response for error handling
        mockMvc.perform(multipart("/api/validate").file(invalidFile))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid JSON content. Please upload a valid JSON file."));
    }

}
