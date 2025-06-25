package com.example.demo.dto;

import java.time.Instant;

public class EnergyCurrentDto {
    private Instant hour;
    private double communityDepleted;
    private double gridPortion;

    public EnergyCurrentDto(Instant hour,
                            double communityDepleted,
                            double gridPortion) {
        this.hour = hour;
        this.communityDepleted = communityDepleted;
        this.gridPortion = gridPortion;
    }

    public Instant getHour() {
        return hour;
    }

    public void setHour(Instant hour) {
        this.hour = hour;
    }

    public double getCommunityDepleted() {
        return communityDepleted;
    }

    public void setCommunityDepleted(double communityDepleted) {
        this.communityDepleted = communityDepleted;
    }

    public double getGridPortion() {
        return gridPortion;
    }

    public void setGridPortion(double gridPortion) {
        this.gridPortion = gridPortion;
    }
}
