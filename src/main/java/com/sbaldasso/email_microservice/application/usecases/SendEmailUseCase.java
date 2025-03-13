package com.sbaldasso.email_microservice.application.usecases;

import com.sbaldasso.email_microservice.application.ports.EmailPort;
import com.sbaldasso.email_microservice.application.ports.EmailRepository;
import com.sbaldasso.email_microservice.domain.Email;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class SendEmailUseCase {
    private final EmailPort emailPort;
    private final EmailRepository emailRepository;

    public SendEmailUseCase(EmailPort emailPort, EmailRepository emailRepository) {
        this.emailPort = emailPort;
        this.emailRepository = emailRepository;
    }

    @Transactional
    public CompletableFuture<Email> execute(Email email) {
        try {
            // Validate email
            email.validate();

            // Set initial state
            email.setId(UUID.randomUUID());
            email.setStatus(Email.EmailStatus.PENDING);
            email.setCreatedAt(LocalDateTime.now());
            
            // Save to repository
            final Email savedEmail = emailRepository.save(email);

            CompletableFuture<Void> emailFuture;
            if (savedEmail.getTemplate() != null && savedEmail.getTemplateModel() != null) {
                emailFuture = emailPort.sendTemplatedEmail(savedEmail);
            } else if (savedEmail.getAttachmentPath() != null) {
                emailFuture = emailPort.sendEmailWithAttachment(savedEmail);
            } else {
                emailFuture = emailPort.sendEmail(savedEmail);
            }

            return emailFuture.thenApply(result -> {
                savedEmail.setStatus(Email.EmailStatus.SENT);
                savedEmail.setSentAt(LocalDateTime.now());
                return emailRepository.save(savedEmail);
            }).exceptionally(throwable -> {
                savedEmail.setStatus(Email.EmailStatus.FAILED);
                savedEmail.setErrorMessage(throwable.getMessage());
                return emailRepository.save(savedEmail);
            });
        } catch (Exception e) {
            CompletableFuture<Email> failedFuture = new CompletableFuture<>();
            failedFuture.completeExceptionally(e);
            return failedFuture;
        }
    }
} 