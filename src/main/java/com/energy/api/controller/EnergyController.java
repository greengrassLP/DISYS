package com.energy.api.controller;

import com.energy.api.model.EnergyPercentage;
import com.energy.api.model.UsageData;
import com.energy.api.repository.EnergyPercentageRepository;
import com.energy.api.repository.UsageDataRepository;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;


@RestController
@RequestMapping("/energy")
public class EnergyController {

    private final UsageDataRepository usageRepo;

    private final EnergyPercentageRepository percentageRepo;

    public EnergyController(UsageDataRepository usageRepo, EnergyPercentageRepository percentageRepo) {
        this.usageRepo = usageRepo;
        this.percentageRepo = percentageRepo;

        percentageRepo.save(LocalDateTime.of(2025, 1, 10, 14, 0), new EnergyPercentage(100.0, 5.63));
    }

    @GetMapping("/current")
    public EnergyPercentage getCurrent() {
        return percentageRepo.getCurrent();
    }

    @GetMapping("/historical")
    public List<UsageData> getHistorical() {
        return usageRepo.findAll();
    }
}