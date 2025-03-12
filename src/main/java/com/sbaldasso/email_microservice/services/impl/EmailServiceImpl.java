package com.sbaldasso.email_microservice.services.impl;

import com.sbaldasso.email_microservice.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.io.File;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Async
    @Override
    public CompletableFuture<Void> sendEmail(String to, String subject, String body) {
        return CompletableFuture.runAsync(() -> {
            try {
                MimeMessage message = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true);
                
                helper.setTo(to);
                helper.setSubject(subject);
                helper.setText(body, true);
                
                mailSender.send(message);
            } catch (MessagingException e) {
                throw new RuntimeException("Failed to send email", e);
            }
        });
    }

    @Async
    @Override
    public CompletableFuture<Void> sendTemplatedEmail(String to, String subject, String template, Map<String, Object> templateModel) {
        return CompletableFuture.runAsync(() -> {
            try {
                Context context = new Context();
                context.setVariables(templateModel);
                String htmlContent = templateEngine.process(template, context);

                MimeMessage message = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true);
                
                helper.setTo(to);
                helper.setSubject(subject);
                helper.setText(htmlContent, true);
                
                mailSender.send(message);
            } catch (MessagingException e) {
                throw new RuntimeException("Failed to send templated email", e);
            }
        });
    }

    @Async
    @Override
    public CompletableFuture<Void> sendEmailWithAttachment(String to, String subject, String body, String attachmentPath) {
        return CompletableFuture.runAsync(() -> {
            try {
                MimeMessage message = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true);
                
                helper.setTo(to);
                helper.setSubject(subject);
                helper.setText(body, true);
                
                FileSystemResource file = new FileSystemResource(new File(attachmentPath));
                helper.addAttachment(file.getFilename(), file);
                
                mailSender.send(message);
            } catch (MessagingException e) {
                throw new RuntimeException("Failed to send email with attachment", e);
            }
        });
    }
} 