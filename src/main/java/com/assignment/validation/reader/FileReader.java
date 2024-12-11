package com.assignment.validation.reader;

import java.io.InputStream;

// This interface is used to read the file and process it.
// It is used to read and process data from different file types, such as JSON, CSV, etc.
public interface FileReader {
    int processFile(InputStream inputStream);
}
