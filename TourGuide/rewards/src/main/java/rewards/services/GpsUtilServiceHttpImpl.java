package rewards.services;

import common.dtos.GetAttractionsDTO;
import common.models.localization.Attraction;
import common.models.localization.VisitedLocation;
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
    public VisitedLocation getUserLocation(UUID userId) {
        String params = "?userId=" + userId.toString();
        return restTemplate.exchange(
                "http://localhost:8080/gpsUtil/getUserLocation" + params,
                HttpMethod.GET,
                null,
                VisitedLocation.class
        ).getBody();
    }

    @Override
    public List<Attraction> getAttractions() {
        return Objects.requireNonNull(restTemplate.exchange(
                "http://localhost:8080/gpsUtil/getAttractions",
                HttpMethod.GET,
                null,
                GetAttractionsDTO.class
        ).getBody()).getAttractions();
    }
}
