package gps;

import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;
import models.dto.AttractionDTO;
import models.dto.LocationDTO;
import models.dto.VisitedLocationDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GpsUtilServiceImpl implements GpsUtilService {
    private final GpsUtil gpsUtil;

    public GpsUtilServiceImpl() {
        this.gpsUtil = new GpsUtil();
    }

    public VisitedLocationDTO getUserLocation(UUID userId) {
        VisitedLocation visitedLocation = gpsUtil.getUserLocation(userId);
        return new VisitedLocationDTO(visitedLocation.userId,
                new LocationDTO(visitedLocation.location.latitude, visitedLocation.location.longitude),
                visitedLocation.timeVisited);
    }

    public List<AttractionDTO> getAttractions() {
        List<Attraction> attractions = gpsUtil.getAttractions();
        List<AttractionDTO> attractionDTOS = new ArrayList<>();

        attractions.forEach(a ->
                attractionDTOS.add(new AttractionDTO(a.attractionName, a.city, a.state, a.latitude, a.longitude))
        );
        return attractionDTOS;
    }
}
