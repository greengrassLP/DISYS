package com.example.demo.dto;

import java.time.LocalDateTime;

public class EnergyData {

    private LocalDateTime timestamp;
    private double communityProduced;
    private double communityUsed;
    private double gridUsed;

    public EnergyData() {
    }

    public EnergyData(LocalDateTime timestamp, double communityProduced, double communityUsed, double gridUsed) {
        this.timestamp = timestamp;
        this.communityProduced = communityProduced;
        this.communityUsed = communityUsed;
        this.gridUsed = gridUsed;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public double getCommunityProduced() {
        return communityProduced;
    }

    public void setCommunityProduced(double communityProduced) {
        this.communityProduced = communityProduced;
    }

    public double getCommunityUsed() {
        return communityUsed;
    }

    public void setCommunityUsed(double communityUsed) {
        this.communityUsed = communityUsed;
    }

    public double getGridUsed() {
        return gridUsed;
    }

    public void setGridUsed(double gridUsed) {
        this.gridUsed = gridUsed;
    }

}
