package com.sbaldasso.email_microservice.infrastructure.controllers;

import com.sbaldasso.email_microservice.application.usecases.SendEmailUseCase;
import com.sbaldasso.email_microservice.domain.Email;
import com.sbaldasso.email_microservice.application.ports.EmailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/v1/email")
public class EmailInfrastructureController {

    private final SendEmailUseCase sendEmailUseCase;
    private final EmailRepository emailRepository;

    @Autowired
    public EmailInfrastructureController(SendEmailUseCase sendEmailUseCase, EmailRepository emailRepository) {
        this.sendEmailUseCase = sendEmailUseCase;
        this.emailRepository = emailRepository;
    }

    @PostMapping("/send")
    public CompletableFuture<ResponseEntity<Email>> sendEmail(
            @RequestParam String to,
            @RequestParam String subject,
            @RequestParam String body) {
        Email email = Email.builder()
                .to(to)
                .subject(subject)
                .body(body)
                .build();
        
        return sendEmailUseCase.execute(email)
                .thenApply(ResponseEntity::ok)
                .exceptionally(throwable -> ResponseEntity.internalServerError().build());
    }

    @PostMapping("/send-template")
    public CompletableFuture<ResponseEntity<Email>> sendTemplatedEmail(
            @RequestParam String to,
            @RequestParam String subject,
            @RequestParam String template,
            @RequestBody Map<String, Object> templateModel) {
        Email email = Email.builder()
                .to(to)
                .subject(subject)
                .template(template)
                .templateModel(templateModel)
                .build();
        
        return sendEmailUseCase.execute(email)
                .thenApply(ResponseEntity::ok)
                .exceptionally(throwable -> ResponseEntity.internalServerError().build());
    }

    @PostMapping("/send-with-attachment")
    public CompletableFuture<ResponseEntity<Email>> sendEmailWithAttachment(
            @RequestParam String to,
            @RequestParam String subject,
            @RequestParam String body,
            @RequestParam String attachmentPath) {
        Email email = Email.builder()
                .to(to)
                .subject(subject)
                .body(body)
                .attachmentPath(attachmentPath)
                .build();
        
        return sendEmailUseCase.execute(email)
                .thenApply(ResponseEntity::ok)
                .exceptionally(throwable -> ResponseEntity.internalServerError().build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Email> getEmail(@PathVariable UUID id) {
        return emailRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Email>> getAllEmails() {
        return ResponseEntity.ok(emailRepository.findAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmail(@PathVariable UUID id) {
        emailRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
} 