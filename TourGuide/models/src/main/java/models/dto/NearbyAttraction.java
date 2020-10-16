package models.dto;

import com.jsoniter.annotation.JsonProperty;

public class NearbyAttraction {
    @JsonProperty("latitude")
    double latitude;
    @JsonProperty("longitude")
    double longitude;
    @JsonProperty("distance")
    double distance;
    @JsonProperty("rewardPoints")
    int rewardPoints;

    public NearbyAttraction(double latitude, double longitude, double distance, int rewardPoints) {
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
