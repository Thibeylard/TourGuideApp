package common.dtos;

import common.models.localization.Attraction;

import java.util.List;

public class GetAttractionsDTO {
    private List<Attraction> attractions;

    public GetAttractionsDTO() {
    }

    public GetAttractionsDTO(List<Attraction> attractions) {
        this.attractions = attractions;
    }

    public List<Attraction> getAttractions() {
        return attractions;
    }
}
