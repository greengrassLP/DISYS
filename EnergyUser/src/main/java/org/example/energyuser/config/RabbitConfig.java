package org.example.energyuser.config;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Value("${user.queue.name}")
    private String queueName;

    @Bean
    public Queue energyQueue() {
        return new Queue(queueName, true);
    }
}
