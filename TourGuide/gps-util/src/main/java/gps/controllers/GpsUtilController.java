package gps.controllers;

import gps.services.GpsUtilService;
import models.dto.AttractionListDTO;
import models.dto.VisitedLocationDTO;
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
    public VisitedLocationDTO getUserLocation(@RequestParam("userId") UUID userId) {
        return gpsUtilService.getUserLocation(userId);
    }

    @GetMapping("gpsUtil/getAttractions")
    public AttractionListDTO getAttractions() {
        return new AttractionListDTO(gpsUtilService.getAttractions());
    }

}
