package org.example.usageservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import java.time.ZonedDateTime;

public class RawEnergyMessageDto {
    public enum Type { PRODUCER, USER }
    public enum Association { COMMUNITY, GRID }

    private Type type;
    private Association association;
    private double kwh;

    @JsonProperty("datetime")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private ZonedDateTime timestamp;

    public RawEnergyMessageDto() {}

    public Type getType() { return type; }
    public void setType(Type type) { this.type = type; }

    public Association getAssociation() { return association; }
    public void setAssociation(Association association) { this.association = association; }

    public double getKwh() { return kwh; }
    public void setKwh(double kwh) { this.kwh = kwh; }

    public ZonedDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(ZonedDateTime timestamp) { this.timestamp = timestamp; }


}