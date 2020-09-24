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
import java.util.concurrent.*;

public class Tracker extends Thread {
	private final Logger logger = LoggerFactory.getLogger(Tracker.class);
	private static final long trackingPollingInterval = TimeUnit.MINUTES.toSeconds(5);
	private final GpsUtil gpsUtil;
	private final RewardsService rewardsService;
	private final ExecutorService executorService = Executors.newSingleThreadExecutor();
	private final TourGuideService tourGuideService;
	private boolean stop = false;

    @Override
    public void run() {
        while (true) {
            if (Thread.currentThread().isInterrupted() || stop) {
                logger.debug("Tracker stopping");
                break;
            }

            trackUsersLocation(tourGuideService.getAllUsers());

            try {
                logger.debug("Tracker sleeping");
                TimeUnit.SECONDS.sleep(trackingPollingInterval);
            } catch (InterruptedException e) {

                break;
            }
        }

    }

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

    private void trackUsersLocation(List<User> users) {
        ForkJoinPool forkJoinPool = new ForkJoinPool(10);
        forkJoinPool.invoke(new TrackingTask(users));
    }

    private void trackUserLocation(User user) {
        VisitedLocation visitedLocation = gpsUtil.getUserLocation(user.getUserId());
        user.addToVisitedLocations(visitedLocation);
        rewardsService.calculateRewards(user);
    }

    /**********************************************************************************
     *
     * Methods Below: For Testing
     *
     **********************************************************************************/

    @Profile("test")
    public void measureTrackingPerformance(List<User> users) {
        trackUsersLocation(users);
    }

    private class TrackingTask extends RecursiveTask<Boolean> {

        private final List<User> trackedUsers;

        public TrackingTask(List<User> users) {
            this.trackedUsers = users;
        }

        /**
         * The main computation performed by this task.
         *
         * @return the result of the computation
         */
        @Override
        protected Boolean compute() {
            if (trackedUsers.size() == 1) {
                trackUserLocation(trackedUsers.get(0));

            } else {
                int midpoint = trackedUsers.size() / 2;
                TrackingTask leftTracking = new TrackingTask(trackedUsers.subList(0, midpoint));
                TrackingTask rightTracking = new TrackingTask(trackedUsers.subList(midpoint, trackedUsers.size()));

                leftTracking.fork();
                rightTracking.compute();
                leftTracking.join();
            }

            return Boolean.TRUE;
        }
    }

}
