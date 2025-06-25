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

@Service
public class PercentageListenerService {

    private static final Logger log = LoggerFactory.getLogger(PercentageListenerService.class);

    private final CurrentPercentageRepository repository;
    private final ObjectMapper mapper = new ObjectMapper();

    public PercentageListenerService(CurrentPercentageRepository repository) {
        this.repository = repository;
    }

    /**
     * Liest stündliche Usage-Updates, berechnet Prozente
     * und speichert CurrentPercentage in der DB.
     */
    @Transactional
    @RabbitListener(queues = "${update.queue.name}")
    public void onUpdate(String messageJson) {
        try {
            JsonNode node = mapper.readTree(messageJson);

            Instant hour    = Instant.parse(node.get("hour").asText());
            double produced = node.get("communityProduced").asDouble();
            double used     = node.get("communityUsed").asDouble();
            double grid     = node.get("gridUsed").asDouble();

            double communityDepleted = produced > 0
                    ? Math.min(used / produced, 1.0) * 100.0
                    : 0.0;
            double totalUsed = used + grid;
            double gridPortion = totalUsed > 0
                    ? (grid / totalUsed) * 100.0
                    : 0.0;

            CurrentPercentage cp = new CurrentPercentage();
            cp.setHour(hour);
            cp.setCommunityDepleted(communityDepleted);
            cp.setGridPortion(gridPortion);

            repository.save(cp);

            log.info("Saved CurrentPercentage [hour={}, communityDepleted={}%, gridPortion={}%]",
                    hour, communityDepleted, gridPortion);

        } catch (Exception e) {
            log.error("Failed to process update message: {}", messageJson, e);
        }
    }
}
