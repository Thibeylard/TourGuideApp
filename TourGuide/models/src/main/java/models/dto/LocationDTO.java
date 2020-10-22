package models.dto;

public class LocationDTO {
    public double latitude;
    public double longitude;

    public LocationDTO() {
    }

    public LocationDTO(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

}
