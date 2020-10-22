package rewards.services;


import common.models.localization.Attraction;
import common.models.localization.Location;
import common.models.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CompletableFuture;

@Service
public class RewardsServiceHttpImpl implements RewardsService {
    private RestTemplate restTemplate;

    @Autowired
    public RewardsServiceHttpImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void setProximityBuffer(int proximityBuffer) {
    }

    public void setDefaultProximityBuffer() {
    }

    public CompletableFuture<?> calculateRewards(User user) {
        return null;
    }

    public boolean isWithinAttractionProximity(Attraction attraction, Location location) {
        return false;
    }

    public int getRewardPoints(Attraction attraction, User user) {
        return -1;
    }

    public double getDistance(Location loc1, Location loc2) {
        return 0;
    }
}