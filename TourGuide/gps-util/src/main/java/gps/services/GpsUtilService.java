package gps.services;

import models.dto.AttractionDTO;
import models.dto.VisitedLocationDTO;

import java.util.List;
import java.util.UUID;

public interface GpsUtilService {

    public VisitedLocationDTO getUserLocation(UUID userId);

    public List<AttractionDTO> getAttractions();

}
