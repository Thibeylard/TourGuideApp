package tourGuide.tracker;

import gpsUtil.GpsUtil;
import gpsUtil.location.VisitedLocation;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tourGuide.service.RewardsService;
import tourGuide.service.TourGuideService;
import tourGuide.user.User;

import java.util.List;
import java.util.concurrent.CountDownLatch;
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
    private final int THREAD_NUMBER = 11;
    private final ExecutorService executorService = Executors.newFixedThreadPool(THREAD_NUMBER);
    private boolean stop = false;
    private CountDownLatch countDownLatch;

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
        StopWatch stopWatch = new StopWatch();
        while (true) {
            if (Thread.currentThread().isInterrupted() || stop) {
                logger.debug("Tracker stopping");
                break;
            }

            List<User> users = tourGuideService.getAllUsers();
            logger.debug("Begin Tracker. Tracking " + users.size() + " users.");

            stopWatch.start();
            List<List<User>> subsets = ListUtils.partition(users, users.size() % THREAD_NUMBER);
            countDownLatch = new CountDownLatch(subsets.size());
            subsets.forEach(userList -> {
                executorService.submit(() -> {
                    userList.forEach(user -> {
                        VisitedLocation visitedLocation = gpsUtil.getUserLocation(user.getUserId());
                        user.addToVisitedLocations(visitedLocation);
                        rewardsService.calculateRewards(user);
                    });
                    countDownLatch.countDown();
                });
            });
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            stopWatch.stop();

            logger.debug("Tracker Time Elapsed: " + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()) + " seconds.");
            stopWatch.reset();
            try {
                logger.debug("Tracker sleeping");
                TimeUnit.SECONDS.sleep(trackingPollingInterval);
            } catch (InterruptedException e) {

                break;
            }
        }

    }

    public boolean isTracking() {
        return countDownLatch != null && countDownLatch.getCount() > 0;
    }

    public void waitTrackingCompletion() {
        //TODO replace busy waiting by awake and restart process
        while (!this.isTracking()) {
        }

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
