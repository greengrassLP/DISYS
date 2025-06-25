package com.example.restapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TotalEnergyBetweenDates {
    // Jackson serialisiert dieses Feld als "communityProduced"
    @JsonProperty("communityProduced")
    private double totalCommunityProduced;

    // Jackson serialisiert dieses Feld als "communityUsed"
    @JsonProperty("communityUsed")
    private double totalCommunityUsed;

    // Jackson serialisiert dieses Feld als "gridUsed"
    @JsonProperty("gridUsed")
    private double totalGridUsed;

    public TotalEnergyBetweenDates() {}

    public TotalEnergyBetweenDates(double totalCommunityProduced,
                                   double totalCommunityUsed,
                                   double totalGridUsed) {
        this.totalCommunityProduced = totalCommunityProduced;
        this.totalCommunityUsed    = totalCommunityUsed;
        this.totalGridUsed         = totalGridUsed;
    }

    public double getTotalCommunityProduced() {
        return totalCommunityProduced;
    }
    public void setTotalCommunityProduced(double val) {
        this.totalCommunityProduced = val;
    }

    public double getTotalCommunityUsed() {
        return totalCommunityUsed;
    }
    public void setTotalCommunityUsed(double val) {
        this.totalCommunityUsed = val;
    }

    public double getTotalGridUsed() {
        return totalGridUsed;
    }
    public void setTotalGridUsed(double val) {
        this.totalGridUsed = val;
    }

    @Override
    public String toString() {
        return "TotalEnergyBetweenDates{" +
                "totalCommunityProduced=" + totalCommunityProduced +
                ", totalCommunityUsed=" + totalCommunityUsed +
                ", totalGridUsed=" + totalGridUsed +
                '}';
    }
}
