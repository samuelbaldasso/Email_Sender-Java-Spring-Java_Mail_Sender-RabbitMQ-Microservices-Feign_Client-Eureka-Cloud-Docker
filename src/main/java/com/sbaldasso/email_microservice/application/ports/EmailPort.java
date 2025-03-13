package com.sbaldasso.email_microservice.application.ports;

import com.sbaldasso.email_microservice.domain.Email;
import java.util.concurrent.CompletableFuture;

public interface EmailPort {
    CompletableFuture<Void> sendEmail(Email email);
    CompletableFuture<Void> sendTemplatedEmail(Email email);
    CompletableFuture<Void> sendEmailWithAttachment(Email email);
} 