package com.sbaldasso.email_microservice.infrastructure.adapters;

import com.sbaldasso.email_microservice.application.ports.EmailPort;
import com.sbaldasso.email_microservice.domain.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.io.File;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Component
public class EmailAdapter implements EmailPort {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Autowired
    public EmailAdapter(JavaMailSender mailSender, TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    @Async
    @Override
    public CompletableFuture<Void> sendEmail(Email email) {
        return CompletableFuture.runAsync(() -> {
            try {
                MimeMessage message = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true);
                
                helper.setTo(email.getTo());
                helper.setSubject(email.getSubject());
                helper.setText(email.getBody(), true);
                
                mailSender.send(message);
            } catch (MessagingException e) {
                throw new RuntimeException("Failed to send email", e);
            }
        });
    }

    @SuppressWarnings("unchecked")
    @Async
    @Override
    public CompletableFuture<Void> sendTemplatedEmail(Email email) {
        return CompletableFuture.runAsync(() -> {
            try {
                Context context = new Context();
                context.setVariables((Map<String, Object>) email.getTemplateModel());
                String htmlContent = templateEngine.process(email.getTemplate(), context);

                MimeMessage message = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true);
                
                helper.setTo(email.getTo());
                helper.setSubject(email.getSubject());
                helper.setText(htmlContent, true);
                
                mailSender.send(message);
            } catch (MessagingException e) {
                throw new RuntimeException("Failed to send templated email", e);
            }
        });
    }

    @Async
    @Override
    public CompletableFuture<Void> sendEmailWithAttachment(Email email) {
        return CompletableFuture.runAsync(() -> {
            try {
                MimeMessage message = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true);
                
                helper.setTo(email.getTo());
                helper.setSubject(email.getSubject());
                helper.setText(email.getBody(), true);
                
                FileSystemResource file = new FileSystemResource(new File(email.getAttachmentPath()));
                helper.addAttachment(file.getFilename(), file);
                
                mailSender.send(message);
            } catch (MessagingException e) {
                throw new RuntimeException("Failed to send email with attachment", e);
            }
        });
    }
} 