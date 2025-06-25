package org.example.usageservice.service;

import org.example.usageservice.dto.RawEnergyMessageDto;
import org.example.usageservice.dto.UsageUpdateDto;
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

    @Value("${update.queue.name}")
    private String updateQueue;

    public UsageListenerService(HourlyUsageRepository repo, RabbitTemplate rabbit) {
        this.repo = repo;
        this.rabbit = rabbit;
    }

    @Transactional
    @RabbitListener(queues = "${raw.queue.name}")
    public void handleRaw(RawEnergyMessageDto msg) {
        // Stunde runden
        ZonedDateTime hour = msg.getTimestamp().truncatedTo(ChronoUnit.HOURS);
        HourlyUsage u = repo.findById(hour)
                .orElseGet(() -> new HourlyUsage(hour));

        if (msg.getType() == RawEnergyMessageDto.Type.PRODUCER) {
            u.setCommunityProduced(u.getCommunityProduced() + msg.getKwh());
        } else {
            u.setCommunityUsed(u.getCommunityUsed() + msg.getKwh());
            double surplus = u.getCommunityProduced() - u.getCommunityUsed();
            if (surplus < 0) {
                u.setGridUsed(u.getGridUsed() + -surplus);
            }
        }

        repo.save(u);
        rabbit.convertAndSend(updateQueue, new UsageUpdateDto(hour));
    }
}