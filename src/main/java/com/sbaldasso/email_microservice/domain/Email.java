package com.sbaldasso.email_microservice.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class Email {
    private UUID id;

    @NotBlank(message = "Recipient email address is required")
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@(.+)$", message = "Invalid email address format")
    private String to;

    @NotBlank(message = "Email subject is required")
    private String subject;

    @NotBlank(message = "Email body is required")
    private String body;

    private String template;
    private Object templateModel;
    private String attachmentPath;

    @NotNull(message = "Email status is required")
    private EmailStatus status;

    @NotNull(message = "Creation timestamp is required")
    private LocalDateTime createdAt;

    private LocalDateTime sentAt;
    private String errorMessage;

    public enum EmailStatus {
        PENDING,
        SENT,
        FAILED
    }

    public void validate() {
        if (template != null && templateModel == null) {
            throw new IllegalStateException("Template model is required when using a template");
        }
        if (template == null && templateModel != null) {
            throw new IllegalStateException("Template is required when providing a template model");
        }
    }
}