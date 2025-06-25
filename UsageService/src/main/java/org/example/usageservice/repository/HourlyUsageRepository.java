package org.example.usageservice.repository;

import org.example.usageservice.model.HourlyUsage;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.Instant;
import java.time.ZonedDateTime;

public interface HourlyUsageRepository extends JpaRepository<HourlyUsage, ZonedDateTime> {

}
