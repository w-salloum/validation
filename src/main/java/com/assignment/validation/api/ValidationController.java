package com.assignment.validation.api;

import com.assignment.validation.dto.Report;
import com.assignment.validation.service.ValidationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
/**
 * Controller responsible for validating uploaded files.
 *
 * Provides an endpoint to upload a file for validation and retrieve a report.
 */
@RestController
@RequestMapping("/api")
public class ValidationController {

    private final ValidationService validationService;

    public ValidationController(ValidationService validationService) {
        this.validationService = validationService;
    }

    /**
     * Validates the uploaded file and generates a report.
     *
     * This endpoint accepts a file upload, validates its content, and returns a report with
     * information about valid and invalid transactions.
     *
     * @param file The file to be validated.
     * @return A report containing the validation results.
     */

    @PostMapping("/validate")
    public ResponseEntity<Report> validate(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(validationService.validate(file));
    }
}
