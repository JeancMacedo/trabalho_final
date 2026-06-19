package com.example.msemail.service;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.example.msemail.dto.EmailRecordDto;
import com.example.msemail.model.EmailModel;
import com.example.msemail.model.EmailStatus;
import com.example.msemail.repository.EmailRepository;

@Service
public class EmailService {
    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender javaMailSender;
    private final EmailRepository emailRepository;
    private final String emailFrom;

    public EmailService(JavaMailSender javaMailSender,
                        EmailRepository emailRepository,
                        @Value("${spring.mail.username:}") String emailFrom) {
        this.javaMailSender = javaMailSender;
        this.emailRepository = emailRepository;
        this.emailFrom = emailFrom;
    }

    public EmailModel sendEmail(EmailRecordDto dto) {
        EmailModel model = new EmailModel();
        model.setUserId(dto.getUserId());
        model.setEmailFrom(emailFrom);
        model.setEmailTo(dto.getEmailTo());
        model.setSubject(dto.getSubject());
        model.setText(dto.getText());
        model.setSendDateEmail(LocalDateTime.now());

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(emailFrom);
            message.setTo(dto.getEmailTo());
            message.setSubject(dto.getSubject());
            message.setText(dto.getText());
            javaMailSender.send(message);
            model.setStatus(EmailStatus.SENT);
        } catch (Exception e) {
            log.error("Falha ao enviar e-mail para {}", dto.getEmailTo(), e);
            model.setStatus(EmailStatus.ERROR);
        }

        return emailRepository.save(model);
    }
}
