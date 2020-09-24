package tourGuide.tracker;

import gpsUtil.GpsUtil;
import gpsUtil.location.VisitedLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import tourGuide.service.RewardsService;
import tourGuide.service.TourGuideService;
import tourGuide.user.User;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Tracker extends Thread {
    private final Logger logger = LoggerFactory.getLogger(Tracker.class);
    private final TourGuideService tourGuideService;
    private final GpsUtil gpsUtil;
    private final RewardsService rewardsService;
    private final long trackingPollingInterval = TimeUnit.MINUTES.toSeconds(5);
    // Concurrency
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private boolean stop = false;

    public Tracker(TourGuideService tourGuideService, GpsUtil gpsUtil, RewardsService rewardsService) {
        this.tourGuideService = tourGuideService;
        this.gpsUtil = gpsUtil;
        this.rewardsService = rewardsService;
    }

    public void startTracking() {
        stop = false;
        executorService.submit(this);
    }

    /**
     * Assures to shut down the Tracker thread
     */
    public void stopTracking() {
        stop = true;
        executorService.shutdownNow();
    }

    @Override
    public void run() {
        while (true) {
            if (Thread.currentThread().isInterrupted() || stop) {
                logger.debug("Tracker stopping");
                break;
            }

            tourGuideService.getAllUsers().forEach(this::trackUserLocation);

            try {
                logger.debug("Tracker sleeping");
                TimeUnit.SECONDS.sleep(trackingPollingInterval);
            } catch (InterruptedException e) {

                break;
            }
        }

    }

    private CompletableFuture<?> trackUserLocation(User user) {
        return CompletableFuture.runAsync(() -> {
            VisitedLocation visitedLocation = gpsUtil.getUserLocation(user.getUserId());
            user.addToVisitedLocations(visitedLocation);
            rewardsService.calculateRewards(user);
        });
    }


    /**********************************************************************************
     *
     * Methods Below: For Testing
     *
     **********************************************************************************/

    @Profile("test")
    public void measureTrackingPerformance(List<User> users) {
        CompletableFuture<?>[] futures = users.stream()
                .map(this::trackUserLocation)
                .toArray(CompletableFuture[]::new);
        CompletableFuture.allOf(futures).join();
    }

}
