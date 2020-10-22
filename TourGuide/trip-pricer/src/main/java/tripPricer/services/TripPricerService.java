package tripPricer.services;

import models.dto.ProviderDTO;

import java.util.List;
import java.util.UUID;

public interface TripPricerService {

    List<ProviderDTO> getPrice(String apiKey, UUID attractionId, int adults, int children, int nightsStay, int rewardsPoints);

    String getProviderName(String apiKey, int adults);
}
