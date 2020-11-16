package tourGuide.tracker;

import common.models.user.User;
import common.services.GpsUtilService;
import common.services.RewardsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tourGuide.services.TourGuideService;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Tracker extends Thread {
    private final Logger logger = LoggerFactory.getLogger(Tracker.class);
    private final TourGuideService tourGuideService;
    private final GpsUtilService gpsUtil;
    private final RewardsService rewardsService;
    private final long trackingPollingInterval = TimeUnit.MINUTES.toSeconds(5);
    // Concurrency
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private boolean stop = true;

    public Tracker(TourGuideService tourGuideService, GpsUtilService gpsUtil, RewardsService rewardsService) {
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
        logger.debug("Tracker shutting down");
        executorService.shutdownNow();
    }

    @Override
    public void run() {
        while (true) {
            if (Thread.currentThread().isInterrupted() || stop) {
                logger.debug("Tracker stopping");
                break;
            }

            CompletableFuture<?>[] futures = tourGuideService.getAllUsers().stream()
                    .map(this::trackUserLocation)
                    .toArray(CompletableFuture[]::new);
            CompletableFuture.allOf(futures).join(); // waiting so that Tracker does not keep asking CompletableFutures

            try {
                logger.debug("Tracker sleeping");
                TimeUnit.SECONDS.sleep(trackingPollingInterval);
            } catch (InterruptedException e) {

                break;
            }
        }

    }

    public CompletableFuture<?> trackUserLocation(User user) {
        return CompletableFuture.supplyAsync(() -> gpsUtil.getUserLocation(user.getUserId()))
                .thenAccept(user::addToVisitedLocations)
                .thenRunAsync(() -> rewardsService.calculateRewards(user));
    }

    public boolean isStopped() {
        return stop;
    }
}
