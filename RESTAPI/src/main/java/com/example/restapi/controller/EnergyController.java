package com.example.restapi.controller;

import com.example.restapi.dto.TotalEnergyBetweenDates;
import com.example.restapi.repository.EnergyDB;
import com.example.restapi.repository.EnergyDataEntity;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/energy")
public class EnergyController {
    private final EnergyDB energyDbRepository;

    public EnergyController(EnergyDB energyDbRepository) {
        this.energyDbRepository = energyDbRepository;
    }

    @GetMapping("/all")
    public List<EnergyDataEntity> getAll() {
        return energyDbRepository.findAll();
    }

    @GetMapping("/between")
    public TotalEnergyBetweenDates getTotalsBetween(
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime start,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime end) {
        Object[] sums = energyDbRepository.findTotalValuesBetweenDates(start, end);
        double prod = ((Number)sums[0]).doubleValue();
        double used = ((Number)sums[1]).doubleValue();
        double grid = ((Number)sums[2]).doubleValue();
        return new TotalEnergyBetweenDates(prod, used, grid);
    }

    @GetMapping("/totals")
    public TotalEnergyBetweenDates getAllTimeTotals() {
        double prod = energyDbRepository.selectCommunityProducedTotals();
        double used = energyDbRepository.selectCommunityUsedTotals();
        double grid = energyDbRepository.selectGridUsedTotals();
        return new TotalEnergyBetweenDates(prod, used, grid);
    }
}