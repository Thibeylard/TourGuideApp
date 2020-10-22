package tripPricer.services;

import common.models.marketing.Provider;
import org.springframework.stereotype.Service;
import tripPricer.TripPricer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class TripPricerServiceImpl implements TripPricerService {
    private final TripPricer tripPricer;

    public TripPricerServiceImpl() {
        this.tripPricer = new TripPricer();
    }

    public List<Provider> getPrice(String apiKey, UUID attractionId, int adults, int children, int nightsStay, int rewardsPoints) {
        List<tripPricer.Provider> providers = tripPricer.getPrice(apiKey, attractionId, adults, children, nightsStay, rewardsPoints);
        List<Provider> providerDTOS = new ArrayList<>();

        providers.forEach(p ->
                providerDTOS.add(new Provider(p.tripId, p.name, p.price)));

        return providerDTOS;
    }

    public String getProviderName(String apiKey, int adults) {
        return tripPricer.getProviderName(apiKey, adults);
    }
}
