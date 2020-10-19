package tripPricer;

import models.dto.ProviderDTO;

import java.util.List;
import java.util.UUID;

public interface TripPricerService {

    public List<ProviderDTO> getPrice(String apiKey, UUID attractionId, int adults, int children, int nightsStay, int rewardsPoints);

    public String getProviderName(String apiKey, int adults);
}
