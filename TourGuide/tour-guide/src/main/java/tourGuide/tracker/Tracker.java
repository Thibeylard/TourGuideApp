package tourGuide.tracker;

import gps.GpsUtilServiceImpl;
import models.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rewards.RewardsServiceImpl;
import tourGuide.service.TourGuideService;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Tracker extends Thread {
    private final Logger logger = LoggerFactory.getLogger(Tracker.class);
    private final TourGuideService tourGuideService;
    private final GpsUtilServiceImpl gpsUtil;
    private final RewardsServiceImpl rewardsServiceImpl;
    private final long trackingPollingInterval = TimeUnit.MINUTES.toSeconds(5);
    // Concurrency
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private boolean stop = true;

    public Tracker(TourGuideService tourGuideService, GpsUtilServiceImpl gpsUtil, RewardsServiceImpl rewardsServiceImpl) {
        this.tourGuideService = tourGuideService;
        this.gpsUtil = gpsUtil;
        this.rewardsServiceImpl = rewardsServiceImpl;
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
                .thenRunAsync(() -> rewardsServiceImpl.calculateRewards(user));
    }

    public boolean isStopped() {
        return stop;
    }
}
