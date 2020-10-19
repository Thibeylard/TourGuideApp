package rewards;

import models.dto.AttractionDTO;
import models.dto.LocationDTO;
import models.user.User;

import java.util.concurrent.CompletableFuture;

public interface RewardsService {

    public void setProximityBuffer(int proximityBuffer);

    public void setDefaultProximityBuffer();

    public CompletableFuture<?> calculateRewards(User user);

    public boolean isWithinAttractionProximity(AttractionDTO attraction, LocationDTO location);

    public int getRewardPoints(AttractionDTO attraction, User user);

    public double getDistance(LocationDTO loc1, LocationDTO loc2);
}
