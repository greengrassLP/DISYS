package org.example.usageservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "hourly_usage")
public class HourlyUsage {
    @Id
    @Column(name = "hour", nullable = false)
    private Instant hour;

    @Column(name = "community_produced", nullable = false)
    private double communityProduced = 0.0;

    @Column(name = "community_used", nullable = false)
    private double communityUsed = 0.0;

    @Column(name = "grid_used", nullable = false)
    private double gridUsed = 0.0;

    // Getter & Setter
    public Instant getHour() { return hour; }
    public void setHour(Instant hour) { this.hour = hour; }
    public double getCommunityProduced() { return communityProduced; }
    public void setCommunityProduced(double v) { this.communityProduced = v; }
    public double getCommunityUsed() { return communityUsed; }
    public void setCommunityUsed(double v) { this.communityUsed = v; }
    public double getGridUsed() { return gridUsed; }
    public void setGridUsed(double v) { this.gridUsed = v; }
}
