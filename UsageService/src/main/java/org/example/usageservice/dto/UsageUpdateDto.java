package org.example.usageservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import java.time.ZonedDateTime;

public class UsageUpdateDto {
    @JsonProperty("hour")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private ZonedDateTime hour;

    public UsageUpdateDto() {}
    public UsageUpdateDto(ZonedDateTime hour) { this.hour = hour; }

    public ZonedDateTime getHour() { return hour; }
    public void setHour(ZonedDateTime hour) { this.hour = hour; }
}


