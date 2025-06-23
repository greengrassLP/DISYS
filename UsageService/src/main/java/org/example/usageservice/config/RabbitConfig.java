package org.example.usageservice.config;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
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
}
