package com.example.demo.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

public class RabbitConfig {

    /** Queue, in die Producer und User ihre Roh-Events posten */
    @Value("${raw.queue.name}")
    private String rawQueueName;

    /** Queue, in die UsageService seine Update-Events postet */
    @Value("${update.queue.name}")
    private String updateQueueName;

    @Bean
    public Queue rawQueue() {
        // Durable=true: Queue überlebt RabbitMQ-Restart
        return new Queue(rawQueueName, true);
    }

    @Bean
    public Queue updateQueue() {
        return new Queue(updateQueueName, true);
    }

    // 1. JSON‐Converter bean
    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    // 2. RabbitTemplate mit JSON‐Converter
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory cf, Jackson2JsonMessageConverter conv) {
        RabbitTemplate template = new RabbitTemplate(cf);
        template.setMessageConverter(conv);
        return template;
    }

    // 3. Listener‐Container Factory mit JSON‐Converter
    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory cf,
            Jackson2JsonMessageConverter conv
    ) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(cf);
        factory.setMessageConverter(conv);
        return factory;
    }
}
