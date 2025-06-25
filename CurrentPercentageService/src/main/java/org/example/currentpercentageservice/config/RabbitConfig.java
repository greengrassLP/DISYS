package org.example.currentpercentageservice.config;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Value("${producer.queue.name}")
    private String producerQueueName;

    @Value("${user.queue.name}")
    private String userQueueName;

    @Bean
    public Queue producerQueue() {
        return new Queue(producerQueueName, true);
    }

    @Bean
    public Queue userQueue() {
        return new Queue(userQueueName, true);
    }
}
