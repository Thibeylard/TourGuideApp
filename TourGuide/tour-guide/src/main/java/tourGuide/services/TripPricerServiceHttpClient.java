package tourGuide.services;


import common.dtos.GetPriceDTO;
import common.models.marketing.Provider;
import common.services.TripPricerService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
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
        GetPriceDTO price = new GetPriceDTO(
                apiKey,
                attractionId,
                adults,
                children,
                nightsStay,
                rewardsPoints);
        try {
            price = restTemplate.exchange(
                    new RequestEntity<>(price, HttpMethod.POST, new URI(basePath + "/tripPricer/getPrice")),
                    GetPriceDTO.class
            ).getBody();
        } catch (URISyntaxException e) {
            //TODO handle exception
            e.printStackTrace();
        }

        return price == null ? new ArrayList<>() : price.getProviders();
    }

    @Override
    public String getProviderName(String apiKey, int adults) {
        String params = "?apiKey=" + apiKey + "&adults=" + adults;
        return restTemplate.exchange(
                basePath + "/tripPricer/getProviderName" + params,
                HttpMethod.GET,
                null,
                String.class
        ).getBody();
    }
}