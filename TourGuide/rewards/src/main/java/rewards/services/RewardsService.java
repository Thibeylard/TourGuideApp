package rewards.services;

import models.dto.AttractionDTO;
import models.dto.LocationDTO;
import models.user.User;

import java.util.concurrent.CompletableFuture;

public interface RewardsService {

    void setProximityBuffer(int proximityBuffer);

    void setDefaultProximityBuffer();

    CompletableFuture<?> calculateRewards(User user);

    boolean isWithinAttractionProximity(AttractionDTO attraction, LocationDTO location);

    int getRewardPoints(AttractionDTO attraction, User user);

    double getDistance(LocationDTO loc1, LocationDTO loc2);
}
