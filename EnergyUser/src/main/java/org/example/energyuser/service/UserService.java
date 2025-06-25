package org.example.energyuser.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class UserService {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public UserService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Value("${user.queue.name}")
    private String queueName;

    /**
     * Sendet alle 1–5 Sekunden eine USER-Nachricht,
     * mit Verbrauch abhängig von der Tageszeit.
     */
    @Scheduled(fixedRate = 5000)
    public void sendUsage() {
        // Tageszeit-Faktor (z.B. mehr Verbrauch morgens/abends)
        int hour = ZonedDateTime.now().getHour();
        double timeFactor = (hour >= 6 && hour < 9) || (hour >= 17 && hour < 21)
                ? ThreadLocalRandom.current().nextDouble(0.005, 0.012)
                : ThreadLocalRandom.current().nextDouble(0.001, 0.005);

        Map<String,Object> msg = new HashMap<>();
        msg.put("type",        "USER");
        msg.put("association", "COMMUNITY");
        msg.put("kwh",         timeFactor);
        msg.put("datetime",    ZonedDateTime.now().toString());

        try {
            rabbitTemplate.convertAndSend(queueName, msg);
            System.out.printf("[User] Sent to '%s': %s%n", queueName, msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
