package org.example.energyuser.service;

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
public class UserService {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RestTemplate restTemplate;

    public UserService(RabbitTemplate rabbitTemplate, RestTemplate restTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        this.restTemplate = restTemplate;
    }

    @Value("${user.queue.name}")
    private String queueName;

    @Value("${weather.api.url}")
    private String weatherApiUrl;

    /**
     * Sendet alle 1–5 Sekunden eine USER-Nachricht,
     * mit Verbrauch basierend auf Wetterbedingungen.
     */
    @Scheduled(fixedDelayString = "#{T(java.util.concurrent.ThreadLocalRandom).current().nextInt(5000,30000)}")
    public void sendUsage() {
        try {
            // Fetch real weather data
            Map<?, ?> weatherData = restTemplate.getForObject(weatherApiUrl, Map.class);
            if (weatherData == null) {
                throw new RuntimeException("Failed to fetch weather data");
            }

            // Extract weather conditions
            double temperature = 20.0;
            double windSpeed = 0.0;
            double clouds = 50.0;
            
            if (weatherData.get("main") instanceof Map) {
                Object temp = ((Map<?, ?>)weatherData.get("main")).get("temp");
                if (temp instanceof Number) {
                    temperature = ((Number)temp).doubleValue();
                }
            }
            
            if (weatherData.get("wind") instanceof Map) {
                Object speed = ((Map<?, ?>)weatherData.get("wind")).get("speed");
                if (speed instanceof Number) {
                    windSpeed = ((Number)speed).doubleValue();
                }
            }
            
            if (weatherData.get("clouds") instanceof Map) {
                Object cloudCover = ((Map<?, ?>)weatherData.get("clouds")).get("all");
                if (cloudCover instanceof Number) {
                    clouds = ((Number)cloudCover).doubleValue();
                }
            }

            // Calculate consumption based on weather conditions
            double consumption = calculateConsumptionBasedOnWeather(temperature, windSpeed, clouds);

            // Create message with real data
            Map<String, Object> msg = new HashMap<>();
            msg.put("type", "USER");
            msg.put("association", "COMMUNITY");
            msg.put("kwh", consumption);
            msg.put("datetime", ZonedDateTime.now().toString());

            // Send message
            String json = objectMapper.writeValueAsString(msg);
            rabbitTemplate.convertAndSend(queueName, json);
            System.out.printf("[User] Sent to '%s': %s%n", queueName, json);

        } catch (Exception e) {
            System.err.println("Error in UserService: " + e.getMessage());
        }
    }

    private double calculateConsumptionBasedOnWeather(double temperature, double windSpeed, double clouds) {
        // Base consumption
        double consumption = 0.02; // ~20 kWh base consumption
        
        // Temperature impact (higher consumption when it's very cold or very hot)
        if (temperature < 15) {
            consumption += (15 - temperature) * 0.005; // Increase consumption when cold
        } else if (temperature > 25) {
            consumption += (temperature - 25) * 0.005; // Increase consumption when hot
        }
        
        // Wind speed impact (lower consumption when windy due to more natural ventilation)
        if (windSpeed > 5) {
            consumption *= 0.9; // Reduce consumption by 10% when windy
        }
        
        // Cloud impact (higher consumption when cloudy due to less natural light)
        double cloudFactor = 1.0 + (clouds / 100.0) * 0.2; // Up to 20% more consumption when cloudy
        consumption *= cloudFactor;
        
        // Time of day impact
        int hour = ZonedDateTime.now().getHour();
        if ((hour >= 6 && hour < 9) || (hour >= 17 && hour < 21)) {
            consumption *= 1.5; // Increase consumption during peak hours
        }
        
        // Add some variation
        double variation = ThreadLocalRandom.current().nextDouble(-0.01, 0.01); // ±10% variation
        return consumption * (1 + variation);
    }
}
