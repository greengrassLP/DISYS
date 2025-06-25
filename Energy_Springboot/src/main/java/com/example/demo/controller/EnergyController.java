package com.example.demo.controller;

import com.example.demo.dto.EnergyCurrentDto;
import com.example.demo.dto.EnergyData;
import com.example.demo.dto.EnergyUsageDto;
import com.example.demo.service.EnergyService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;


@RestController
@RequestMapping("/energy")
public class EnergyController {

    private final EnergyService energyService;

    public EnergyController(EnergyService energyService) {
        this.energyService = energyService;
    }

    @GetMapping("/current")
    public EnergyCurrentDto getCurrent() {
        return energyService.getCurrent();
    }

    @GetMapping("/historical")
    public List<EnergyUsageDto> getHistorical(
            @RequestParam
            @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime start,

            @RequestParam
            @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime end) {
        return energyService.getHistorical(start, end);
    }
}