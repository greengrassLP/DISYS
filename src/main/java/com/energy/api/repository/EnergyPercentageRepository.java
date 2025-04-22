package com.energy.api.repository;

import com.energy.api.model.EnergyPercentage;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class EnergyPercentageRepository {

    private final Map<LocalDateTime, EnergyPercentage> percentageByHour = new HashMap<>();

    public void save(LocalDateTime hour, EnergyPercentage percentage) {
        percentageByHour.put(hour, percentage);
    }

    public EnergyPercentage findByHour(LocalDateTime hour) {
        return percentageByHour.get(hour);
    }

    public EnergyPercentage getCurrent() {
        return percentageByHour.values().stream()
                .reduce((first, second) -> second)
                .orElse(new EnergyPercentage(0.0, 0.0));
    }

    public void clear() {
        percentageByHour.clear();
    }
}