package com.example.msemail.mq;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import com.example.msemail.dto.EmailDTO;

@Service
public class EmailConsumer {
    @RabbitListener(queues = "${broker.queue.email.name:default.email}")
    public void receive(EmailDTO dto) {
        System.out.println("[EmailConsumer] Received message for: " + dto.getTo());
        System.out.println("Subject: " + dto.getSubject());
        System.out.println("Text: " + dto.getText());
    }
}
