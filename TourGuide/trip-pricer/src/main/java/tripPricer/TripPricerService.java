package tripPricer;

import models.dto.ProviderDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TripPricerService {
    private final TripPricer tripPricer;

    public TripPricerService() {
        this.tripPricer = new TripPricer();
    }

    public List<ProviderDTO> getPrice(String apiKey, UUID attractionId, int adults, int children, int nightsStay, int rewardsPoints) {
        List<Provider> providers = tripPricer.getPrice(apiKey, attractionId, adults, children, nightsStay, rewardsPoints);
        List<ProviderDTO> providerDTOS = new ArrayList<>();

        providers.forEach(p ->
                providerDTOS.add(new ProviderDTO(p.tripId, p.name, p.price)));

        return providerDTOS;
    }

    public String getProviderName(String apiKey, int adults) {
        return tripPricer.getProviderName(apiKey, adults);
    }
}
