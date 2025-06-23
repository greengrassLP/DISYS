package org.example.usageservice.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.usageservice.model.HourlyUsage;
import org.example.usageservice.repository.HourlyUsageRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

@Service
public class UsageListenerService {

    private final HourlyUsageRepository repo;
    private final RabbitTemplate rabbit;
    private final ObjectMapper mapper = new ObjectMapper();

    @Value("${update.queue.name}")
    private String updateQueue;

    public UsageListenerService(HourlyUsageRepository repo, RabbitTemplate rabbit) {
        this.repo = repo;
        this.rabbit = rabbit;
    }

    @Transactional
    @RabbitListener(queues = "${raw.queue.name}")
    public void handleRaw(String json) {
        try {
            JsonNode n = mapper.readTree(json);

            // ZonedDateTime parsen (inkl. Zone-ID) und zu Instant konvertieren
            ZonedDateTime zdt = ZonedDateTime.parse(n.get("datetime").asText());
            Instant ts = zdt.toInstant();
            Instant hour = ts.truncatedTo(ChronoUnit.HOURS);

            HourlyUsage u = repo.findById(hour).orElseGet(() -> {
                HourlyUsage h = new HourlyUsage();
                h.setHour(hour);
                return h;
            });

            double kwh = n.get("kwh").asDouble();
            if ("PRODUCER".equals(n.get("type").asText())) {
                u.setCommunityProduced(u.getCommunityProduced() + kwh);
            } else {
                u.setCommunityUsed(u.getCommunityUsed() + kwh);
                double surplus = u.getCommunityProduced() - u.getCommunityUsed();
                if (surplus < 0) {
                    u.setGridUsed(u.getGridUsed() + (-surplus));
                }
            }

            // Speichern der aktualisierten Stunde
            repo.save(u);

            // Als Update-Event zurückschicken
            String out = mapper.writeValueAsString(u);
            rabbit.convertAndSend(updateQueue, out);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
