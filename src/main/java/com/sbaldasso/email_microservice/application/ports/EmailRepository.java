package com.sbaldasso.email_microservice.application.ports;

import com.sbaldasso.email_microservice.domain.Email;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EmailRepository {
    Email save(Email email);
    Optional<Email> findById(UUID id);
    List<Email> findAll();
    void deleteById(UUID id);
} 