package com.energy.api.model;

public class EnergyPercentage {
    private double communityDepleted;
    private double gridPortion;

    public EnergyPercentage() {}

    public EnergyPercentage(double communityDepleted, double gridPortion) {
        this.communityDepleted = communityDepleted;
        this.gridPortion = gridPortion;
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