package com.energy.api;

import com.energy.api.EnergyData;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.List;

public class ApiService {
    private final String BASE_URL = "http://localhost:8080/energy";
    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    public List<EnergyData> fetchCurrent() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/current"))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return mapper.readValue(response.body(), new TypeReference<>() {});
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    public List<EnergyData> fetchHistorical(LocalDateTime start, LocalDateTime end) {
        try {
            String startStr = start.toString();
            String endStr = end.toString();

            String url = String.format("%s/historical?start=%s&end=%s", BASE_URL, startStr, endStr);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            return mapper.readValue(response.body(), new TypeReference<>() {});
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

}
