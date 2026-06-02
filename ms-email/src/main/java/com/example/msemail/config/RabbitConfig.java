package com.example.msemail.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;

@Configuration
@EnableRabbit
public class RabbitConfig {
    @Value("${broker.queue.email.name:default.email}")
    private String emailQueueName;

    @Bean
    public Queue emailQueue() {
        return new Queue(emailQueueName, true);
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2MessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
