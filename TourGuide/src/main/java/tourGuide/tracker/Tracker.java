package tourGuide.tracker;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tourGuide.service.TourGuideService;
import tourGuide.user.User;

public class Tracker extends Thread {
	private final Logger logger = LoggerFactory.getLogger(Tracker.class);
	private static final long trackingPollingInterval = TimeUnit.MINUTES.toSeconds(5);
	private final ExecutorService executorService = Executors.newSingleThreadExecutor();
	private final TourGuideService tourGuideService;
	private boolean stop = false;
	private final StopWatch stopWatch = new StopWatch();
	boolean testMode = true;

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
			if(trackedUsers.size() == 1){
				tourGuideService.trackUserLocation(trackedUsers.get(0));

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

	public Tracker(TourGuideService tourGuideService) {
		this.tourGuideService = tourGuideService;

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
		while(true) {
			if(Thread.currentThread().isInterrupted() || stop) {
				logger.debug("Tracker stopping");
				break;
			}

			stopWatch.reset();
			List<User> users = tourGuideService.getAllUsers();
			ForkJoinPool forkJoinPool = new ForkJoinPool();
			logger.debug("Begin Tracker. Tracking " + users.size() + " users.");
			stopWatch.start();
			forkJoinPool.invoke(new TrackingTask(users));
			stopWatch.stop();
			logger.debug("Tracker Time Elapsed: " + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()) + " seconds.");

			if(testMode) {
				stop = true;
			} else {
				try {
					logger.debug("Tracker sleeping");
					TimeUnit.SECONDS.sleep(trackingPollingInterval);
				} catch (InterruptedException e) {
					break;
				}
			}

		}
	}

	public boolean isStopped() {
		return stop;
	}
}
