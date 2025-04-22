package com.energy.api.repository;

import com.energy.api.model.UsageData;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class UsageDataRepository {

    private final List<UsageData> usageDataList = new ArrayList<>();

    public UsageDataRepository() {
        // Dummy-Daten beim Start hinzufügen
        usageDataList.add(new UsageData(LocalDateTime.of(2025, 1, 10, 14, 0), 18.05, 18.02, 1.056));
        usageDataList.add(new UsageData(LocalDateTime.of(2025, 1, 10, 13, 0), 15.015, 14.033, 2.049));
        usageDataList.add(new UsageData(LocalDateTime.of(2025, 1, 10, 13, 0), 15.015, 14.033, 2.049));
        usageDataList.add(new UsageData(LocalDateTime.of(2025, 1, 10, 14, 0), 18.05, 18.02, 1.056));
        usageDataList.add(new UsageData(LocalDateTime.of(2025, 1, 10, 15, 0), 17.2, 16.7, 0.9));
    }

    public List<UsageData> findAll() {
        return usageDataList;
    }

    public List<UsageData> findBetween(LocalDateTime start, LocalDateTime end) {
        return usageDataList.stream()
                .filter(data -> !data.getHour().isBefore(start) && !data.getHour().isAfter(end))
                .toList();
    }

    public void save(UsageData data) {
        usageDataList.add(data);
    }

    public void clear() {
        usageDataList.clear();
    }
}
