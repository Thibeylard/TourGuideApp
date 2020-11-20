package tourGuide.services;


import common.models.marketing.Provider;
import common.services.TripPricerService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.UUID;

public class TripPricerServiceHttpClient implements TripPricerService {
    private final RestTemplate restTemplate;
    @Value("${trip-pricer.base-path}")
    private String basePath;

    public TripPricerServiceHttpClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public List<Provider> getPrice(String apiKey, UUID attractionId, int adults, int children, int nightsStay, int rewardsPoints) {
        return null;
    }

    @Override
    public String getProviderName(String apiKey, int adults) {
        return null;
    }
}