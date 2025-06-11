package com.example.demo.controller;

import com.example.demo.dto.EnergyData;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;


@RestController
@RequestMapping("/energy")
public class EnergyController {

    @GetMapping("/current")
    public List<EnergyData> getCurrent() {
        List<EnergyData> currentData = new ArrayList<>();
        // Community produziert zwischen 40 und 100 kWh
        double produced = ThreadLocalRandom.current().nextDouble(40, 100);

        // Community verwendet zwischen 60 % und 90 % der Produktion
        double usagePercentage = ThreadLocalRandom.current().nextDouble(0.6, 0.9);
        double used = produced * usagePercentage;

        // Rest kommt aus dem Grid (z. B. 0 bis 10 % zusätzlich)
        double gridUsed = produced - used;
        if (gridUsed < 0) gridUsed = 0;

        currentData.add(new EnergyData(
                LocalDateTime.now(),
                Math.round(produced * 10.0) / 10.0,
                Math.round(used * 10.0) / 10.0,
                Math.round(gridUsed * 10.0) / 10.0
        ));

        return currentData;
    }

    @GetMapping("/historical")
    public List<EnergyData> getHistorical(
            @RequestParam
            @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime start,

            @RequestParam
            @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime end) {
        List<EnergyData> historicalData = new ArrayList<>();

        historicalData.add(new EnergyData(start.plusHours(1), 40, 50, 10));
        historicalData.add(new EnergyData(start.plusHours(1), 36, 6, 0));
        historicalData.add(new EnergyData(start.plusHours(1), 321, 78, 0));
        historicalData.add(new EnergyData(end.minusHours(1), 532, 423, 13));
        historicalData.add(new EnergyData(end.minusHours(1), 76, 64, 7));

        return historicalData;
    }
}