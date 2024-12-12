package com.assignment.validation.reader;

import com.assignment.validation.dto.Transaction;
import com.assignment.validation.exception.InvalidJSONContentException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class JSONFileReader implements FileReader {

    @Override
    public List<Transaction> readFile(InputStream inputStream) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            // Parse JSON into a list of Transaction objects
            return  objectMapper.readValue(inputStream, new TypeReference<>() {});

        } catch (IOException e) {
            throw new InvalidJSONContentException("Invalid JSON content. Please upload a valid JSON file.");
        }
    }
}
