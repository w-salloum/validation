package com.assignment.validation.utils;

public class TestData {

    public static final String JSON_INVALID_CONTENT = """
            [
                INVALID JSON CONTENT
            ]
            """;
    public static final String JSON_NO_ERRORS = """
            [
                {
                    "reference": 194261,
                    "accountNumber": "NL91RABO0315273637",
                    "description": "Clothes from Jan Bakker",
                    "startBalance": 21.6,
                    "mutation": -41.83,
                    "endBalance": -20.23
                },
                {
                    "reference": 112806,
                    "accountNumber": "NL27SNSB0917829871",
                    "description": "Clothes for Willem Dekker",
                    "startBalance": 91.23,
                    "mutation": 15.57,
                    "endBalance": 106.8
                }
            ]
            """;
    public static final String JSON_DUPLICATE_REFERENCE = """
            [
                {
                    "reference": 194261,
                    "accountNumber": "NL91RABO0315273637",
                    "description": "Clothes from Jan Bakker",
                    "startBalance": 21.6,
                    "mutation": -41.83,
                    "endBalance": -20.23
                },
                {
                    "reference": 194261,
                    "accountNumber": "NL27SNSB0917829871",
                    "description": "Clothes for Willem Dekker",
                    "startBalance": 91.23,
                    "mutation": 15.57,
                    "endBalance": 106.8
                }
            ]
            """;
    public static final String JSON_INCORRECT_END_BALANCE = """
            [
                {
                    "reference": 194261,
                    "accountNumber": "NL91RABO0315273637",
                    "description": "Clothes from Jan Bakker",
                    "startBalance": 21.6,
                    "mutation": -41.83,
                    "endBalance": 0.0
                },
                {
                    "reference": 112806,
                    "accountNumber": "NL27SNSB0917829871",
                    "description": "Clothes for Willem Dekker",
                    "startBalance": 91.23,
                    "mutation": 15.57,
                    "endBalance": 106.8
                }
            ]
            """;
    public static final String CSV_INVALID_CONTENT = "INVALID";
    public static final String CSV_NO_ERRORS = """
            Reference,AccountNumber,Description,Start Balance,Mutation,End Balance
            194261,NL91RABO0315273637,Clothes from Jan Bakker,21.6,-41.83,-20.23
            112806,NL27SNSB0917829871,Clothes for Willem Dekker,91.23,15.57,106.8
            """;
    public static final String CSV_DUPLICATE_REFERENCE = """
            Reference,AccountNumber,Description,Start Balance,Mutation,End Balance
            194261,NL91RABO0315273637,Clothes from Jan Bakker,21.6,-41.83,-20.23
            194261,NL27SNSB0917829871,Clothes for Willem Dekker,91.23,15.57,106.8
            """;
    public static final String CSV_INCORRECT_END_BALANCE = """
            Reference,AccountNumber,Description,Start Balance,Mutation,End Balance
            194261,NL91RABO0315273637,Clothes from Jan Bakker,21.6,-41.83,0.0
            112806,NL27SNSB0917829871,Clothes for Willem Dekker,91.23,15.57,106.8
            """;
}
