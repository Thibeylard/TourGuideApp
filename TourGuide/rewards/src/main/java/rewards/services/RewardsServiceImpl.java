package rewards.services;


import common.models.localization.Attraction;
import common.models.localization.Location;
import common.models.localization.VisitedLocation;
import common.models.user.User;
import gps.services.GpsUtilService;
import org.springframework.stereotype.Service;
import rewardCentral.RewardCentral;

import java.util.ArrayList;
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
    private final List<Attraction> attractions;

    // Concurrency
    private final ExecutorService executorService = Executors.newFixedThreadPool(50);
    private int proximityBuffer = defaultProximityBuffer;

    public RewardsServiceImpl(GpsUtilService gpsUtil) {
        attractions = gpsUtil.getAttractions();
        this.rewardsCentral = new RewardCentral();
    }

    public void setProximityBuffer(int proximityBuffer) {
        this.proximityBuffer = proximityBuffer;
    }

    public void setDefaultProximityBuffer() {
        proximityBuffer = defaultProximityBuffer;
    }

    public CompletableFuture<?> calculateRewards(User user) {
        List<CompletableFuture<?>> futures = new ArrayList<>();
        futures.add(CompletableFuture.runAsync(() ->
                user.getVisitedLocations().forEach(ul -> {
                    attractions.stream()
                            .filter(a -> nearAttraction(ul, a))
                            .forEach(a -> {
                                if (user.getUserRewards().stream().noneMatch(r -> r.attraction.attractionName.equals(a.attractionName))) {
                                    user.addUserReward(new common.models.user.UserReward(ul, a, getRewardPoints(a, user)));
                                }
                            });
                })));

        return CompletableFuture.allOf(futures.stream().toArray(CompletableFuture[]::new));
    }

    public boolean isWithinAttractionProximity(Attraction attraction, Location location) {
        return !(getDistance(attraction, location) > attractionProximityRange);
    }

    private boolean nearAttraction(VisitedLocation visitedLocation, Attraction attraction) {
        return !(getDistance(attraction, visitedLocation.location) > proximityBuffer);
    }

    public int getRewardPoints(Attraction attraction, User user) {
        return rewardsCentral.getAttractionRewardPoints(attraction.attractionId, user.getUserId());
    }

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