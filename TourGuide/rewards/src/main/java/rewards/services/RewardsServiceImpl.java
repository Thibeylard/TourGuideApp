package rewards.services;


import common.models.localization.Attraction;
import common.models.localization.Location;
import common.models.localization.VisitedLocation;
import common.models.user.User;
import common.models.user.UserReward;
import gps.services.GpsUtilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rewardCentral.RewardCentral;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class RewardsServiceImpl implements RewardsService {
    private static final double STATUTE_MILES_PER_NAUTICAL_MILE = 1.15077945;

    // proximity in miles
    private final int defaultProximityBuffer = 10;
    private final int attractionProximityRange = 200;
    private final RewardCentral rewardsCentral;
    private final GpsUtilService gpsUtilService;
    private List<Attraction> attractions;

    // Concurrency
    private final ExecutorService executorService = Executors.newFixedThreadPool(50);
    private int proximityBuffer = defaultProximityBuffer;

    @Autowired
    public RewardsServiceImpl(GpsUtilService gpsUtilService) {
        this.gpsUtilService = gpsUtilService;
        this.attractions = gpsUtilService.getAttractions();
        this.rewardsCentral = new RewardCentral();
    }

    @Override
    public void updateAttractions() {
        this.attractions = gpsUtilService.getAttractions();
    }

    @Override
    public void setProximityBuffer(int proximityBuffer) {
        this.proximityBuffer = proximityBuffer;
    }

    @Override
    public void setDefaultProximityBuffer() {
        proximityBuffer = defaultProximityBuffer;
    }

    @Override
    public CompletableFuture<?> calculateRewards(User user) {
        return CompletableFuture.supplyAsync(() -> {
            user.getVisitedLocations().forEach(ul -> {
                attractions.stream()
                        .filter(a -> nearAttraction(ul, a))
                        .forEach(a -> {
                            if (user.getUserRewards().stream().noneMatch(r -> r.attraction.attractionName.equals(a.attractionName))) {
                                user.addUserReward(new UserReward(ul, a, getRewardPoints(a, user)));
                            }
                        });
            });
            return user;
        });
    }

    @Override
    public boolean isWithinAttractionProximity(Attraction attraction, Location location) {
        return !(getDistance(attraction, location) > attractionProximityRange);
    }

    private boolean nearAttraction(VisitedLocation visitedLocation, Attraction attraction) {
        return !(getDistance(attraction, visitedLocation.location) > proximityBuffer);
    }

    @Override
    public int getRewardPoints(Attraction attraction, User user) {
        return rewardsCentral.getAttractionRewardPoints(attraction.attractionId, user.getUserId());
    }

    @Override
    public double getDistance(Location loc1, Location loc2) {
        double lat1 = Math.toRadians(loc1.latitude);
        double lon1 = Math.toRadians(loc1.longitude);
        double lat2 = Math.toRadians(loc2.latitude);
        double lon2 = Math.toRadians(loc2.longitude);

        double angle = Math.acos(Math.sin(lat1) * Math.sin(lat2)
                + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon1 - lon2));

        double nauticalMiles = 60 * Math.toDegrees(angle);
        return STATUTE_MILES_PER_NAUTICAL_MILE * nauticalMiles;
    }

    public int getDefaultProximityBuffer() {
        return defaultProximityBuffer;
    }

    public int getProximityBuffer() {
        return proximityBuffer;
    }
}