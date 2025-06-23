package org.example.currentpercentageservice.config;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Value("${update.queue.name}")
    private String updateQueueName;

    @Bean
    public Queue updateQueue() {
        return new Queue(updateQueueName, true);
    }
}
