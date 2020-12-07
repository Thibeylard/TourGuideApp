package common.dtos;


public class NearbyAttractionDTO {
    double latitude;
    double longitude;
    double distance;
    int rewardPoints;

    public NearbyAttractionDTO() {
    }

    public NearbyAttractionDTO(double latitude, double longitude, double distance, int rewardPoints) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.distance = distance;
        this.rewardPoints = rewardPoints;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getDistance() {
        return distance;
    }

    public int getRewardPoints() {
        return rewardPoints;
    }
}
