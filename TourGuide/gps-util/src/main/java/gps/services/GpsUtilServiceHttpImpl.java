package gps.services;

import models.dto.AttractionDTO;
import models.dto.AttractionListDTO;
import models.dto.VisitedLocationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class GpsUtilServiceHttpImpl implements GpsUtilService {

    private RestTemplate restTemplate;

    @Autowired
    public GpsUtilServiceHttpImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public VisitedLocationDTO getUserLocation(UUID userId) {
        String params = "?userId=" + userId.toString();
        return restTemplate.exchange(
                "http://localhost:8080/gpsUtil/getUserLocation" + params,
                HttpMethod.GET,
                null,
                VisitedLocationDTO.class
        ).getBody();
    }

    @Override
    public List<AttractionDTO> getAttractions() {
        return Objects.requireNonNull(restTemplate.exchange(
                "http://localhost:8080/gpsUtil/getAttractions",
                HttpMethod.GET,
                null,
                AttractionListDTO.class
        ).getBody()).getAttractions();
    }
}
