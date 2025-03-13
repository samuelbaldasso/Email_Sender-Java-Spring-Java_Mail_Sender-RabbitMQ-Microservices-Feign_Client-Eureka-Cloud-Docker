package com.sbaldasso.email_microservice.infrastructure.persistence;

import com.sbaldasso.email_microservice.domain.Email;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "emails")
@Data
public class EmailEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String toAddress;

    @Column(nullable = false)
    private String subject;

    @Column(columnDefinition = "TEXT")
    private String body;

    private String template;

    @Column(columnDefinition = "JSON")
    private String templateModel;

    private String attachmentPath;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Email.EmailStatus status;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime sentAt;

    @Column(columnDefinition = "TEXT")
    private String errorMessage;
} 