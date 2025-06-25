package com.example.demo.repository;

import com.example.demo.model.HourlyUsage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

public interface HourlyUsageRepository extends JpaRepository<HourlyUsage, Instant> {
    List<HourlyUsage> findAllByHourBetween(Instant start, Instant end);
}
