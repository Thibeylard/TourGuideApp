package common.services;

import common.models.localization.Attraction;
import common.models.localization.Location;
import common.models.user.User;

import java.util.concurrent.CompletableFuture;

public interface RewardsService {

    void setProximityBuffer(int proximityBuffer);

    void setDefaultProximityBuffer();

    void updateAttractions();

    CompletableFuture<?> calculateRewards(User user);

    boolean isWithinAttractionProximity(Attraction attraction, Location location);

    int getRewardPoints(Attraction attraction, User user);

    double getDistance(Location loc1, Location loc2);

}
