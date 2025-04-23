package com.energy.api;

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

    public double getCommunityProduced() {
        return communityProduced;
    }

    public double getCommunityUsed() {
        return communityUsed;
    }

    public double getGridUsed() {
        return gridUsed;
    }

}
