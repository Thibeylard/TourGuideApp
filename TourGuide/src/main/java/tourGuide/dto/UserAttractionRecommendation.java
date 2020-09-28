package tourGuide.dto;

import com.jsoniter.annotation.JsonProperty;
import gpsUtil.location.Location;

import java.util.Map;

public class UserAttractionRecommendation {
    @JsonProperty("userPosition")
    Location userPosition;
    @JsonProperty("nearbyAttractions")
    Map<String, NearbyAttraction> nearbyAttractions;

    public UserAttractionRecommendation(Location userPosition, Map<String, NearbyAttraction> nearbyAttractions) {
        this.userPosition = userPosition;
        this.nearbyAttractions = nearbyAttractions;
    }

    public Location getUserPosition() {
        return userPosition;
    }

    public Map<String, NearbyAttraction> getNearbyAttractions() {
        return nearbyAttractions;
    }
}
