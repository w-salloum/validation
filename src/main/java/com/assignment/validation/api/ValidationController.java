package com.assignment.validation.api;

import com.assignment.validation.dto.Report;
import com.assignment.validation.service.ValidationService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
public class ValidationController {

    private final ValidationService validationService;
    public ValidationController(ValidationService validationService) {
        this.validationService = validationService;
    }

    @PostMapping("/validate")
    public Report validate(@RequestParam("file") MultipartFile file) {
        return validationService.validate(file);
    }

}
