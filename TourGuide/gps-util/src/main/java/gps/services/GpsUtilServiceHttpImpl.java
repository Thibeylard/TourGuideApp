package gps.services;

import models.dto.AttractionDTO;
import models.dto.VisitedLocationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
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
        return null;
    }

    @Override
    public List<AttractionDTO> getAttractions() {
        return null;
    }
}
