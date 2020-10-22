package models.dto;

import java.util.Date;
import java.util.UUID;

public class VisitedLocationDTO {
    public UUID userId;
    public LocationDTO location;
    public Date timeVisited;

    public VisitedLocationDTO() {
    }

    public VisitedLocationDTO(UUID userId, LocationDTO location, Date timeVisited) {
        this.userId = userId;
        this.location = location;
        this.timeVisited = timeVisited;
    }
}
