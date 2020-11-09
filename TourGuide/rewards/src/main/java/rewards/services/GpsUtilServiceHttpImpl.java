package rewards.services;

import common.dtos.GetAttractionsDTO;
import common.models.localization.Attraction;
import common.models.localization.VisitedLocation;
import gps.services.GpsUtilService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class GpsUtilServiceHttpImpl implements GpsUtilService {

    private final RestTemplate restTemplate;
    @Value("${gps.base-path}")
    private String basePath;

    public GpsUtilServiceHttpImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public VisitedLocation getUserLocation(UUID userId) {
        String params = "?userId=" + userId.toString();
        return restTemplate.exchange(
                basePath + "/gpsUtil/getUserLocation" + params,
                HttpMethod.GET,
                null,
                VisitedLocation.class
        ).getBody();
    }

    @Override
    public List<Attraction> getAttractions() {
        return Objects.requireNonNull(restTemplate.exchange(
                basePath + "/gpsUtil/getAttractions",
                HttpMethod.GET,
                null,
                GetAttractionsDTO.class
        ).getBody()).getAttractions();
    }
}
