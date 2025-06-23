package org.example.energyproducer.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class ProducerService {

    private final RabbitTemplate rabbitTemplate;
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ProducerService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Value("${weather.api.url}")
    private String weatherApiUrl;

    @Value("${producer.queue.name}")
    private String queueName;

    /**
     * Alle 1–5 Sekunden eine PRODUCER-Nachricht senden,
     * nach dem EchoInProducer-Vorbild aus dem Lecture-Code.
     */
    @Scheduled(fixedDelayString = "#{T(java.util.concurrent.ThreadLocalRandom).current().nextInt(1000,5000)}")
    public void sendProduction() {
        try {
            // 1) Wetter abfragen
            Map<?,?> weather = restTemplate.getForObject(weatherApiUrl, Map.class);
            double clouds = 50.0;
            if (weather != null && weather.get("clouds") instanceof Map) {
                Object all = ((Map<?,?>)weather.get("clouds")).get("all");
                if (all instanceof Number) {
                    clouds = ((Number)all).doubleValue();
                }
            }
            double factor = 1.0 - clouds / 100.0;

            // 2) kWh berechnen
            double kwh = ThreadLocalRandom.current().nextDouble(0.001, 0.005) * factor;

            // 3) Nachricht zusammenbauen
            Map<String,Object> msg = new HashMap<>();
            msg.put("type",        "PRODUCER");
            msg.put("association", "COMMUNITY");
            msg.put("kwh",         kwh);
            msg.put("datetime",    ZonedDateTime.now().toString());

            // 4) Senden (JSON-String)
            String json = objectMapper.writeValueAsString(msg);
            rabbitTemplate.convertAndSend(queueName, json);
            System.out.printf("[Producer] Sent to '%s': %s%n", queueName, json);

        } catch (Exception e) {
            System.err.println("Error in ProducerService: " + e.getMessage());
        }
    }
}
