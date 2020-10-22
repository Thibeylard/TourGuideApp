package gps.services;

import common.models.localization.Attraction;
import common.models.localization.Location;
import common.models.localization.VisitedLocation;
import gpsUtil.GpsUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class GpsUtilServiceImpl implements GpsUtilService {
    private final GpsUtil gpsUtil;

    public GpsUtilServiceImpl() {
        this.gpsUtil = new GpsUtil();
    }

    public VisitedLocation getUserLocation(UUID userId) {

        gpsUtil.location.VisitedLocation visitedLocation = gpsUtil.getUserLocation(userId);
        return new VisitedLocation(visitedLocation.userId,
                new Location(visitedLocation.location.latitude, visitedLocation.location.longitude),
                visitedLocation.timeVisited);
    }

    public List<Attraction> getAttractions() {
        List<gpsUtil.location.Attraction> attractions = gpsUtil.getAttractions();
        List<Attraction> attractionDTOS = new ArrayList<>();

        attractions.forEach(a ->
                attractionDTOS.add(new Attraction(a.attractionName, a.city, a.state, a.latitude, a.longitude))
        );
        return attractionDTOS;
    }
}
