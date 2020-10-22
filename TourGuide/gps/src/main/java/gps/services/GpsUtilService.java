package gps.services;

import common.models.localization.Attraction;
import common.models.localization.VisitedLocation;

import java.util.List;
import java.util.UUID;

public interface GpsUtilService {

    public VisitedLocation getUserLocation(UUID userId);

    public List<Attraction> getAttractions();

}
