# File Validation API

This project provides a Spring Boot-based API for validating uploaded files and generating reports based on the contents of the file. It is designed to process files (such as JSON or CSV) containing transactions and provide a report that indicates the number of valid and invalid transactions, along with any errors.

## Features

- Endpoint to upload a file for validation.
- Supports multiple file types (e.g., JSON, CSV).
- Returns a report with the number of valid and invalid transactions.
- Provides detailed error messages for invalid transactions.

## Technologies Used

- **Java 21**
- **Spring Boot**

## API Endpoints

### POST `/api/validate`

This endpoint accepts a file upload, validates its content, and returns a report with information about valid and invalid transactions.

#### Request

- **Method**: `POST`
- **Endpoint**: `/api/validate`
- **Content-Type**: `multipart/form-data`
- **Request Body**: A file (e.g., `.json`, `.csv`) to be validated.

Example of using curl to upload a file:

```bash
curl -X POST -F "file=@path/to/your/file.json" http://localhost:8080/api/validate
```
#### Response

- **Status**: 200 OK
- **Body**: A JSON report containing the following fields:
- **numberOfValidTransactions**: The number of valid transactions found in the file.
- **numberOfInvalidTransactions**: The number of invalid transactions found in the file.
- **transactionErrors**: An array of errors encountered during validation (if any).

Example response:
```bash
{
    "numberOfValidTransactions": 8,
    "numberOfInvalidTransactions": 2,
    "transactionErrors": [
        {
            "reference": 112806,
            "errors": [
                "Duplicate Reference"
            ]
        },
        {
            "reference": 112806,
            "errors": [
                "Duplicate Reference"
            ]
        }
    ]
}
```

## How to run 
**Wihout docker**
- `./gradlew clean build`
- `./gradlew bootRun`
- 
**Wih docker**
  - `./gradlew clean build`
  - `sudo docker compose up  --build`
    
Then the service will be available on http://localhost:8080/api/validate
