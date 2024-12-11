package com.assignment.validation.api;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
public class ValidationController {

    @PostMapping("/validate")
    public void validate(@RequestParam("file") MultipartFile file) {
        // validate pdf content
    }

}
