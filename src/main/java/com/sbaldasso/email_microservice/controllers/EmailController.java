package com.sbaldasso.email_microservice.controllers;

import com.sbaldasso.email_microservice.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/v1/email")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/send")
    public CompletableFuture<ResponseEntity<Void>> sendEmail(
            @RequestParam String to,
            @RequestParam String subject,
            @RequestParam String body) {
        return emailService.sendEmail(to, subject, body)
                .thenApply(result -> ResponseEntity.ok().build())
                .exceptionally(throwable -> ResponseEntity.internalServerError().build());
    }

    @PostMapping("/send-template")
    public CompletableFuture<ResponseEntity<Void>> sendTemplatedEmail(
            @RequestParam String to,
            @RequestParam String subject,
            @RequestParam String template,
            @RequestBody Map<String, Object> templateModel) {
        return emailService.sendTemplatedEmail(to, subject, template, templateModel)
                .thenApply(result -> ResponseEntity.ok().build())
                .exceptionally(throwable -> ResponseEntity.internalServerError().build());
    }

    @PostMapping("/send-with-attachment")
    public CompletableFuture<ResponseEntity<Void>> sendEmailWithAttachment(
            @RequestParam String to,
            @RequestParam String subject,
            @RequestParam String body,
            @RequestParam String attachmentPath) {
        return emailService.sendEmailWithAttachment(to, subject, body, attachmentPath)
                .thenApply(result -> ResponseEntity.ok().build())
                .exceptionally(throwable -> ResponseEntity.internalServerError().build());
    }
}