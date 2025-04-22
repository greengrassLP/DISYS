package com.energy.api.controller;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import com.energy.api.model.EnergyPercentage;
import com.energy.api.model.UsageData;
import com.energy.api.service.ApiService;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class MainController {

    @FXML
    private Label labelCommunityPool;

    @FXML
    private Label labelGridPortion;

    @FXML
    private Label labelCommunityProduced;

    @FXML
    private Label labelCommunityUsed;

    @FXML
    private Label labelGridUsed;

    @FXML
    private DatePicker dpStart;

    @FXML
    private DatePicker dpEnd;


    private final ApiService apiService = new ApiService();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    //random Werte generieren für Veranschaulichung
    double produced = 10 + Math.random() * 10;
    double used = produced * 0.8;
    double grid = produced - used;

    @FXML
    public void onRefresh() {
        EnergyPercentage percentage = apiService.fetchCurrent();
        labelCommunityPool.setText("Community Pool: " + percentage.getCommunityDepleted() + " %");
        labelGridPortion.setText("Grid Portion: " + percentage.getGridPortion() + " %");
    }

    @FXML
    public void onShowData() {
        try {
            if (dpStart.getValue() == null || dpEnd.getValue() == null) {
                labelCommunityProduced.setText("Bitte gültiges Start- und Enddatum auswählen.");
                labelCommunityUsed.setText("");
                labelGridUsed.setText("");
                return;
            }

            // Automatischer Start um 00:00:00, Ende um 23:59:59
            LocalDateTime start = dpStart.getValue().atStartOfDay();
            LocalDateTime end = dpEnd.getValue().atTime(23, 59, 59);

            List<UsageData> data = apiService.fetchHistorical(start, end);
            if (!data.isEmpty()) {
                labelCommunityProduced.setText("Community produced: " + produced + " kWh");
                labelCommunityUsed.setText("Community used: " + used + " kWh");
                labelGridUsed.setText("Grid used: " + grid + " kWh");
            } else {
                labelCommunityProduced.setText("No data");
                labelCommunityUsed.setText("");
                labelGridUsed.setText("");
            }
        } catch (Exception e) {
            labelCommunityProduced.setText("Error: invalid input.");
            labelCommunityUsed.setText("");
            labelGridUsed.setText("");
            e.printStackTrace();
        }
    }
}