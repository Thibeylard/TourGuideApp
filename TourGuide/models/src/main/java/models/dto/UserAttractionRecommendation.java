package models.dto;


import com.jsoniter.annotation.JsonProperty;

import java.util.Map;

public class UserAttractionRecommendation {
    @JsonProperty("userPosition")
    LocationDTO userPosition;
    @JsonProperty("nearbyAttractions")
    Map<String, NearbyAttraction> nearbyAttractions;

    public UserAttractionRecommendation(LocationDTO userPosition, Map<String, NearbyAttraction> nearbyAttractions) {
        this.userPosition = userPosition;
        this.nearbyAttractions = nearbyAttractions;
    }

    public LocationDTO getUserPosition() {
        return userPosition;
    }

    public Map<String, NearbyAttraction> getNearbyAttractions() {
        return nearbyAttractions;
    }
}
