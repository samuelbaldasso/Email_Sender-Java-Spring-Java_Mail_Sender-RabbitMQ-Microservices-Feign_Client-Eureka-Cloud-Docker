package com.sbaldasso.email_microservice.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface JpaEmailRepository extends JpaRepository<EmailEntity, UUID> {
} 