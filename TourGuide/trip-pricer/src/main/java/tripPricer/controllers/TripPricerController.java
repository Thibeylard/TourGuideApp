package tripPricer.controllers;

import common.models.marketing.Provider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import tripPricer.services.TripPricerService;

import java.util.List;
import java.util.UUID;

@RestController
public class TripPricerController {
    private final TripPricerService tripPricerService;

    @Autowired
    public TripPricerController(TripPricerService tripPricerService) {
        this.tripPricerService = tripPricerService;
    }

    @GetMapping("tripPricer/getPrice")
    public List<Provider> getPrice(String apiKey, UUID attractionId, int adults, int children, int nightsStay, int rewardsPoints) {
        return tripPricerService.getPrice(apiKey, attractionId, adults, children, nightsStay, rewardsPoints);
    }

    @GetMapping("tripPricer/getProviderName")
    public String getProviderName(String apiKey, int adults) {
        return tripPricerService.getProviderName(apiKey, adults);
    }
}
