package com.energy.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.time.LocalDateTime;
import java.util.List;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


public class MainController {

    @FXML private Label labelCommunityPool;
    @FXML private Label labelGridPortion;
    @FXML private Label labelCommunityProduced;
    @FXML private Label labelCommunityUsed;
    @FXML private Label labelGridUsed;
    @FXML private DatePicker dpStart;
    @FXML private DatePicker dpEnd;
    @FXML private TextField tfStart;
    @FXML private TextField tfEnd;


    private final ApiService apiService = new ApiService();


    @FXML
    public void onRefresh() {
        List<EnergyData> current = apiService.fetchCurrent();

        if (!current.isEmpty()) {
            EnergyData latest = current.getFirst();

            labelCommunityPool.setText("Community Pool: " + latest.getCommunityProduced() + " % used");
            labelGridPortion.setText("Grid Portion: " + latest.getGridUsed() + " %");
        } else {
            labelCommunityPool.setText("Keine Daten erhalten");
            labelGridPortion.setText("");
        }
    }

    @FXML
    public void onShowData() {
        try {
            if (dpStart.getValue() == null || dpEnd.getValue() == null) {
                labelCommunityProduced.setText("Bitte Start- und Enddatum auswählen.");
                labelCommunityUsed.setText("");
                labelGridUsed.setText("");
                return;
            }

            LocalDateTime start = dpStart.getValue().atStartOfDay();
            LocalDateTime end = dpEnd.getValue().atTime(23, 59, 59);

            List<EnergyData> data = apiService.fetchHistorical(start, end);

            if (data.isEmpty()) {
                labelCommunityProduced.setText("Keine Daten gefunden.");
                labelCommunityUsed.setText("");
                labelGridUsed.setText("");
                return;
            }

            // Beispiel: Summe über alle Einträge
            double producedSum = 0;
            double usedSum = 0;
            double gridSum = 0;

            for (EnergyData entry : data) {
                producedSum += entry.getCommunityProduced();
                usedSum += entry.getCommunityUsed();
                gridSum += entry.getGridUsed();
            }

            labelCommunityProduced.setText("Community produced: " + Math.round(producedSum * 10.0) / 10.0 + " kWh");
            labelCommunityUsed.setText("Community used: " + Math.round(usedSum * 10.0) / 10.0 + " kWh");
            labelGridUsed.setText("Grid used: " + Math.round(gridSum * 10.0) / 10.0 + " kWh");

        } catch (Exception e) {
            e.printStackTrace();
            labelCommunityProduced.setText("Fehler bei der Datenabfrage.");
            labelCommunityUsed.setText("");
            labelGridUsed.setText("");
        }
    }

    // just checking if it works
}