package models.dto;


import com.jsoniter.annotation.JsonProperty;

import java.util.Map;

public class AttractionRecommendationDTO {
    @JsonProperty("userPosition")
    LocationDTO userPosition;
    @JsonProperty("nearbyAttractions")
    Map<String, NearbyAttractionDTO> nearbyAttractions;

    public AttractionRecommendationDTO() {
    }

    public AttractionRecommendationDTO(LocationDTO userPosition, Map<String, NearbyAttractionDTO> nearbyAttractions) {
        this.userPosition = userPosition;
        this.nearbyAttractions = nearbyAttractions;
    }

    public LocationDTO getUserPosition() {
        return userPosition;
    }

    public Map<String, NearbyAttractionDTO> getNearbyAttractions() {
        return nearbyAttractions;
    }
}
