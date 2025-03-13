package com.sbaldasso.email_microservice.infrastructure.persistence;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sbaldasso.email_microservice.application.ports.EmailRepository;
import com.sbaldasso.email_microservice.domain.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class EmailRepositoryAdapter implements EmailRepository {

    private final JpaEmailRepository jpaEmailRepository;
    private final ObjectMapper objectMapper;

    @Autowired
    public EmailRepositoryAdapter(JpaEmailRepository jpaEmailRepository, ObjectMapper objectMapper) {
        this.jpaEmailRepository = jpaEmailRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public Email save(Email email) {
        EmailEntity entity = toEntity(email);
        entity = jpaEmailRepository.save(entity);
        return toDomain(entity);
    }

    @Override
    public Optional<Email> findById(UUID id) {
        return jpaEmailRepository.findById(id)
                .map(this::toDomain);
    }

    @Override
    public List<Email> findAll() {
        return jpaEmailRepository.findAll().stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(UUID id) {
        jpaEmailRepository.deleteById(id);
    }

    private EmailEntity toEntity(Email email) {
        EmailEntity entity = new EmailEntity();
        entity.setId(email.getId());
        entity.setToAddress(email.getTo());
        entity.setSubject(email.getSubject());
        entity.setBody(email.getBody());
        entity.setTemplate(email.getTemplate());
        try {
            entity.setTemplateModel(email.getTemplateModel() != null ? 
                objectMapper.writeValueAsString(email.getTemplateModel()) : null);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize template model", e);
        }
        entity.setAttachmentPath(email.getAttachmentPath());
        entity.setStatus(email.getStatus());
        entity.setCreatedAt(email.getCreatedAt());
        entity.setSentAt(email.getSentAt());
        entity.setErrorMessage(email.getErrorMessage());
        return entity;
    }

    private Email toDomain(EmailEntity entity) {
        Object templateModel = null;
        if (entity.getTemplateModel() != null) {
            try {
                templateModel = objectMapper.readValue(entity.getTemplateModel(), Object.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Failed to deserialize template model", e);
            }
        }

        return Email.builder()
                .id(entity.getId())
                .to(entity.getToAddress())
                .subject(entity.getSubject())
                .body(entity.getBody())
                .template(entity.getTemplate())
                .templateModel(templateModel)
                .attachmentPath(entity.getAttachmentPath())
                .status(entity.getStatus())
                .createdAt(entity.getCreatedAt())
                .sentAt(entity.getSentAt())
                .errorMessage(entity.getErrorMessage())
                .build();
    }
} 