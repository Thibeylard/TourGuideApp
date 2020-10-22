package gps.controllers;

import common.dtos.GetAttractionsDTO;
import common.models.localization.VisitedLocation;
import gps.services.GpsUtilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class GpsUtilController {
    private final GpsUtilService gpsUtilService;

    @Autowired()
    public GpsUtilController(@Qualifier("gpsUtilServiceImpl") GpsUtilService gpsUtilService) {
        this.gpsUtilService = gpsUtilService;
    }

    @GetMapping("gpsUtil/getUserLocation")
    public VisitedLocation getUserLocation(@RequestParam("userId") UUID userId) {
        return gpsUtilService.getUserLocation(userId);
    }

    @GetMapping("gpsUtil/getAttractions")
    public GetAttractionsDTO getAttractions() {
        return new GetAttractionsDTO(gpsUtilService.getAttractions());
    }

}
