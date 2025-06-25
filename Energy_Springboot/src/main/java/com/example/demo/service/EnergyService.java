package com.example.demo.service;

import com.example.demo.dto.EnergyCurrentDto;
import com.example.demo.dto.EnergyUsageDto;
import com.example.demo.model.CurrentPercentage;
import com.example.demo.model.HourlyUsage;
import com.example.demo.repository.CurrentPercentageRepository;
import com.example.demo.repository.HourlyUsageRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EnergyService {

    private final HourlyUsageRepository usageRepo;
    private final CurrentPercentageRepository pctRepo;

    public EnergyService(HourlyUsageRepository usageRepo,
                         CurrentPercentageRepository pctRepo) {
        this.usageRepo = usageRepo;
        this.pctRepo   = pctRepo;
    }

    public EnergyCurrentDto getCurrent() {
        Instant nowHour = Instant.now().truncatedTo(ChronoUnit.HOURS);
        CurrentPercentage cp = pctRepo.findById(nowHour)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Keine Daten für Stunde " + nowHour
                ));
        return new EnergyCurrentDto(
                cp.getHour(),
                cp.getCommunityDepleted(),
                cp.getGridPortion()
        );
    }

    public List<EnergyUsageDto> getHistorical(LocalDateTime start, LocalDateTime end) {
        Instant iStart = start.atZone(ZoneOffset.UTC).toInstant();
        Instant iEnd   = end  .atZone(ZoneOffset.UTC).toInstant();

        List<HourlyUsage> usages = usageRepo.findAllByHourBetween(iStart, iEnd);
        return usages.stream()
                .map(u -> new EnergyUsageDto(
                        u.getHour(),
                        u.getCommunityProduced(),
                        u.getCommunityUsed(),
                        u.getGridUsed()
                ))
                .collect(Collectors.toList());
    }
}