package org.example.currentpercentageservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "current_percentage")
public class CurrentPercentage {

    @Id
    @Column(name = "hour", nullable = false)
    private Instant hour;

    /** Anteil der Community-Energie, die verbraucht wurde (in Prozent) */
    @Column(name = "community_depleted", nullable = false)
    private double communityDepleted;

    /** Anteil der Gesamtenergie aus dem Netz (in Prozent) */
    @Column(name = "grid_portion", nullable = false)
    private double gridPortion;

    public CurrentPercentage() {
    }

    public CurrentPercentage(Instant hour, double communityDepleted, double gridPortion) {
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
