package com.energy.api.service;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.energy.api.model.EnergyPercentage;
import com.energy.api.model.UsageData;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

public class ApiService {

    private final String BASE_URL = "http://localhost:8080/energy";
    private final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());
    private final HttpClient client = HttpClient.newHttpClient();

    public EnergyPercentage fetchCurrent() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/current"))
                    .GET()
                    .build();
            String json = client.send(request, HttpResponse.BodyHandlers.ofString()).body();
            return mapper.readValue(json, EnergyPercentage.class);
        } catch (Exception e) {
            e.printStackTrace();
            return new EnergyPercentage(0.0, 0.0);
        }
    }

    public List<UsageData> fetchHistorical(LocalDateTime start, LocalDateTime end) {
        try {
            String startStr = start.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            String endStr = end.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/historical?start=" + startStr + "&ende=" + endStr))
                    .GET()
                    .build();

            String json = client.send(request, HttpResponse.BodyHandlers.ofString()).body();
            return mapper.readValue(json, new TypeReference<>() {});
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}
