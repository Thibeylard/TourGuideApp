package tripPricer.controllers;

import common.dtos.GetPriceDTO;
import common.services.TripPricerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TripPricerController {
    private final TripPricerService tripPricerService;

    @Autowired
    public TripPricerController(TripPricerService tripPricerService) {
        this.tripPricerService = tripPricerService;
    }

    @PostMapping("tripPricer/getPrice")
    public GetPriceDTO getPrice(GetPriceDTO dto) {
        return dto.withProviders(tripPricerService.getPrice(
                dto.getApiKey(),
                dto.getAttractionId(),
                dto.getAdults(),
                dto.getChildren(),
                dto.getNightsStay(),
                dto.getRewardsPoints()));
    }
}
