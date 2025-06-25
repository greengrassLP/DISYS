package com.example.demo.dto;

import java.time.Instant;
import java.time.ZonedDateTime;

public class EnergyUsageDto {
    private ZonedDateTime timestamp;
    private double communityProduced;
    private double communityUsed;
    private double gridUsed;

    public EnergyUsageDto(ZonedDateTime timestamp,
                          double communityProduced,
                          double communityUsed,
                          double gridUsed) {
        this.timestamp = timestamp;
        this.communityProduced = communityProduced;
        this.communityUsed = communityUsed;
        this.gridUsed = gridUsed;
    }

    public ZonedDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(ZonedDateTime timestamp) {
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
