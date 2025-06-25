package org.example.currentpercentageservice.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.currentpercentageservice.model.CurrentPercentage;
import org.example.currentpercentageservice.repository.CurrentPercentageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.ZonedDateTime;

@Service
public class PercentageListenerService {

    private static final Logger log = LoggerFactory.getLogger(PercentageListenerService.class);

    private final CurrentPercentageRepository repository;
    private final ObjectMapper mapper = new ObjectMapper();

    public PercentageListenerService(CurrentPercentageRepository repository) {
        this.repository = repository;
        // Clear existing data on startup
        repository.deleteAll();
    }

    /**
     * Liest stündliche Usage-Updates, berechnet Prozente
     * und speichert CurrentPercentage in der DB.
     */
    @Transactional
    @RabbitListener(queues = {"${producer.queue.name}", "${user.queue.name}"})
    public void onUpdate(String messageJson) {
        try {
            JsonNode node = mapper.readTree(messageJson);

            // Get the datetime and convert to Instant
            String datetimeStr = node.get("datetime").asText();
            ZonedDateTime zdt = ZonedDateTime.parse(datetimeStr);
            Instant instant = zdt.toInstant();
            
            // For USER messages, kwh represents used energy
            // For PRODUCER messages, kwh represents produced energy
            double kwh = node.get("kwh").asDouble();
            double used = node.get("type").asText().equals("USER") ? kwh : 0.0;
            double produced = node.get("type").asText().equals("PRODUCER") ? kwh : 0.0;
            double grid = 0.0; // We don't have grid data in these messages

            double communityDepleted = produced > 0
                    ? Math.min(used / produced, 1.0) * 100.0
                    : 0.0;
            double gridPortion = (produced + grid) > 0
                    ? (grid / (produced + grid)) * 100.0
                    : 0.0;

            CurrentPercentage cp = new CurrentPercentage();
            cp.setHour(instant);
            cp.setCommunityDepleted(communityDepleted);
            cp.setGridPortion(gridPortion);

            repository.save(cp);

            log.info("Saved CurrentPercentage [datetime={}, communityDepleted={}%, gridPortion={}%]",
                    instant, communityDepleted, gridPortion);

        } catch (Exception e) {
            log.error("Failed to process update message: {}", messageJson, e);
        }
    }
}
