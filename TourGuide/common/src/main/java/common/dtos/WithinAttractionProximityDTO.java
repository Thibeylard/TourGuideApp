package common.dtos;

import common.models.localization.Attraction;
import common.models.localization.Location;

public class WithinAttractionProximityDTO {
    private Attraction attraction;
    private Location location;

    public WithinAttractionProximityDTO() {
    }

    public WithinAttractionProximityDTO(Attraction attraction, Location location) {
        this.attraction = attraction;
        this.location = location;
    }

    public Attraction getAttraction() {
        return attraction;
    }

    public Location getLocation() {
        return location;
    }
}
