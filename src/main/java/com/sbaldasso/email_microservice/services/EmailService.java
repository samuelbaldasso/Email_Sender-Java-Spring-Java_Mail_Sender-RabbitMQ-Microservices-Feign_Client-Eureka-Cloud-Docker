package com.sbaldasso.email_microservice.services;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface EmailService {
    CompletableFuture<Void> sendEmail(String to, String subject, String body);
    CompletableFuture<Void> sendTemplatedEmail(String to, String subject, String template, Map<String, Object> templateModel);
    CompletableFuture<Void> sendEmailWithAttachment(String to, String subject, String body, String attachmentPath);
}