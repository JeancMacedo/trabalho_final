package com.example.msuser.mq;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.msuser.dto.EmailDTO;

@Service
public class UserProducer {
    private final RabbitTemplate rabbitTemplate;
    private final String queueName;

    public UserProducer(RabbitTemplate rabbitTemplate, @Value("${broker.queue.email.name:default.email}") String queueName) {
        this.rabbitTemplate = rabbitTemplate;
        this.queueName = queueName;
    }

    public void sendEmail(EmailDTO dto) {
        rabbitTemplate.convertAndSend(queueName, dto);
    }
}
