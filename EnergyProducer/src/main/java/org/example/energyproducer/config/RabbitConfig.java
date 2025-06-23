package org.example.energyproducer.config;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Value("${raw.queue.name}")
    private String queueName;

    @Bean
    public Queue energyQueue() {
        return new Queue(queueName, true);
    }
}
