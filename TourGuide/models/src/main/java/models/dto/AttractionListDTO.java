package models.dto;

import java.util.List;

public class AttractionListDTO {
    private List<AttractionDTO> attractions;

    public AttractionListDTO() {
    }

    public AttractionListDTO(List<AttractionDTO> attractions) {
        this.attractions = attractions;
    }

    public List<AttractionDTO> getAttractions() {
        return attractions;
    }
}
