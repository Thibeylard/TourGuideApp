package common.dtos;

import common.models.localization.Location;

public class GetDistanceDTO {
    private Location firstLocation;
    private Location secondLocation;

    public GetDistanceDTO() {
    }

    public GetDistanceDTO(Location firstLocation, Location secondLocation) {
        this.firstLocation = firstLocation;
        this.secondLocation = secondLocation;
    }

    public Location getFirstLocation() {
        return firstLocation;
    }

    public Location getSecondLocation() {
        return secondLocation;
    }
}
