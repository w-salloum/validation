package com.assignment.validation.reader;


import com.assignment.validation.dto.Transaction;

import java.io.InputStream;
import java.util.List;

// This interface is used to read the file and process it.
// It is used to read and process data from different file types, such as JSON, CSV, etc.
public interface FileReader {
    List<Transaction> readFile(InputStream inputStream);
}
