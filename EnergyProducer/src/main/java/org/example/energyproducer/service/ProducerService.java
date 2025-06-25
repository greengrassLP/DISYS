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
    @Scheduled(fixedDelayString = "#{T(java.util.concurrent.ThreadLocalRandom).current().nextInt(5000,30000)}")
    public void sendProduction() {
        try {
            // Fetch real weather data
            Map<?, ?> weather = restTemplate.getForObject(weatherApiUrl, Map.class);
            if (weather == null) {
                throw new RuntimeException("Failed to fetch weather data");
            }

            // Extract relevant weather data
            double clouds = 50.0;
            double temperature = 20.0;
            double windSpeed = 0.0;
            
            if (weather.get("clouds") instanceof Map) {
                Object all = ((Map<?, ?>)weather.get("clouds")).get("all");
                if (all instanceof Number) {
                    clouds = ((Number)all).doubleValue();
                }
            }
            
            if (weather.get("main") instanceof Map) {
                Object temp = ((Map<?, ?>)weather.get("main")).get("temp");
                if (temp instanceof Number) {
                    temperature = ((Number)temp).doubleValue();
                }
            }
            
            if (weather.get("wind") instanceof Map) {
                Object speed = ((Map<?, ?>)weather.get("wind")).get("speed");
                if (speed instanceof Number) {
                    windSpeed = ((Number)speed).doubleValue();
                }
            }

            // Calculate production based on real weather conditions
            double productionFactor = 1.0 - clouds / 100.0; // Cloud coverage impact
            productionFactor *= (temperature > 0 ? 1.0 : 0.8); // Temperature impact
            productionFactor *= (windSpeed > 0 ? 1.1 : 1.0); // Wind impact

            // Get real time of day from weather data
            Map<?, ?> sys = (Map<?, ?>) weather.get("sys");
            long sunrise = ((Number) sys.get("sunrise")).longValue() * 1000;
            long sunset = ((Number) sys.get("sunset")).longValue() * 1000;
            long now = System.currentTimeMillis();

            // Calculate production based on time of day
            double kwh = 0.0;
            if (now >= sunrise && now <= sunset) {
                // Calculate based on position in the day
                double dayProgress = (now - sunrise) / (double) (sunset - sunrise);
                double peakFactor = Math.sin(dayProgress * Math.PI);
                kwh = 0.1 * peakFactor * productionFactor; // Base production of 100 kWh at peak
            } else {
                // Minimal production during nighttime (10% of peak)
                kwh = 0.01 * productionFactor; // Base production of 10 kWh at night
            }

            // Create message with real data
            Map<String, Object> msg = new HashMap<>();
            msg.put("type", "PRODUCER");
            msg.put("association", "COMMUNITY");
            msg.put("kwh", kwh);
            msg.put("datetime", ZonedDateTime.now().toString());
            msg.put("clouds", clouds);
            msg.put("temperature", temperature);
            msg.put("windSpeed", windSpeed);

            // Send message
            String json = objectMapper.writeValueAsString(msg);
            rabbitTemplate.convertAndSend(queueName, json);
            System.out.printf("[Producer] Sent to '%s': %s%n", queueName, json);

        } catch (Exception e) {
            System.err.println("Error in ProducerService: " + e.getMessage());
        }
    }
}
