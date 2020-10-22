package common.dtos;


import com.jsoniter.annotation.JsonProperty;
import common.models.localization.Location;

import java.util.Map;

public class AttractionRecommendationDTO {
    @JsonProperty("userPosition")
    Location userPosition;
    @JsonProperty("nearbyAttractions")
    Map<String, NearbyAttractionDTO> nearbyAttractions;

    public AttractionRecommendationDTO() {
    }

    public AttractionRecommendationDTO(Location userPosition, Map<String, NearbyAttractionDTO> nearbyAttractions) {
        this.userPosition = userPosition;
        this.nearbyAttractions = nearbyAttractions;
    }

    public Location getUserPosition() {
        return userPosition;
    }

    public Map<String, NearbyAttractionDTO> getNearbyAttractions() {
        return nearbyAttractions;
    }
}
