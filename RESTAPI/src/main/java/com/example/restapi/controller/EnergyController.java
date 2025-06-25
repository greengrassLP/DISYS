package com.example.restapi.controller;

import com.example.restapi.dto.TotalEnergyBetweenDates;
import com.example.restapi.dto.EnergyDataDto;
import com.example.restapi.repository.EnergyDB;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/energy")
public class EnergyController {
    private final EnergyDB energyDbRepository;

    public EnergyController(EnergyDB energyDbRepository) {
        this.energyDbRepository = energyDbRepository;
    }

    // === current als 1-Element-Array ===
    @GetMapping("/current")
    public List<TotalEnergyBetweenDates> getCurrent() {
        TotalEnergyBetweenDates current = new TotalEnergyBetweenDates(
                energyDbRepository.selectCommunityProducedForCurrentHour(),
                energyDbRepository.selectCommunityUsedForCurrentHour(),
                energyDbRepository.selectGridUsedForCurrentHour()
        );
        return Collections.singletonList(current);
    }

    // === historical data mit Timestamp ===
    @GetMapping("/historical")
    public List<EnergyDataDto> getHistorical(
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime start,
            @RequestParam("end")   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime end) {

        return energyDbRepository.findByHourBetween(start, end)
                .stream()
                .map(e -> new EnergyDataDto(
                        e.getHour(),               // LocalDateTime timestamp
                        e.getCommunityProduced(),
                        e.getCommunityUsed(),
                        e.getGridUsed()
                ))
                .toList();
    }

    // optional: /between alias für Kompatibilität
    @GetMapping("/between")
    public List<TotalEnergyBetweenDates> aliasBetween(
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime start,
            @RequestParam("end")   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime end) {

        Object[] sums = energyDbRepository.findTotalValuesBetweenDates(start, end);
        TotalEnergyBetweenDates tot = new TotalEnergyBetweenDates(
                ((Number)sums[0]).doubleValue(),
                ((Number)sums[1]).doubleValue(),
                ((Number)sums[2]).doubleValue()
        );
        return Collections.singletonList(tot);
    }

    // /totals liefert Gesamtwerte ohne Timestamp
    @GetMapping("/totals")
    public TotalEnergyBetweenDates getAllTimeTotals() {
        double prod = energyDbRepository.selectCommunityProducedTotals();
        double used = energyDbRepository.selectCommunityUsedTotals();
        double grid = energyDbRepository.selectGridUsedTotals();
        return new TotalEnergyBetweenDates(prod, used, grid);
    }
}
