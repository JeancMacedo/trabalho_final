package com.example.msemail.mq;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import com.example.msemail.dto.EmailRecordDto;
import com.example.msemail.service.EmailService;

@Service
public class EmailConsumer {
    private final EmailService emailService;

    public EmailConsumer(EmailService emailService) {
        this.emailService = emailService;
    }

    @RabbitListener(queues = "${broker.queue.email.name:default.email}")
    public void receive(EmailRecordDto dto) {
        System.out.println("[EmailConsumer] Received message for userId=" + dto.getUserId() + " email=" + dto.getEmailTo());
        System.out.println("[EmailConsumer] Subject=" + dto.getSubject());
        System.out.println("[EmailConsumer] Text=" + dto.getText());
        emailService.sendEmail(dto);
    }
}
