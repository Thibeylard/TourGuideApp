package tourGuide.unit;

import gps.services.GpsUtilServiceImpl;
import models.dto.AttractionDTO;
import models.dto.VisitedLocationDTO;
import models.user.User;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import rewards.services.RewardsServiceImpl;
import tourGuide.helper.InternalTestHelper;
import tourGuide.service.TourGuideService;

import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class TestPerformance {

    @Autowired
    private GpsUtilServiceImpl gpsUtilServiceImpl;
    @Autowired
    private RewardsServiceImpl rewardsServiceImpl;

    /*
     * A note on performance improvements:
     *
     *     The number of users generated for the high volume tests can be easily adjusted via this method:
     *
     *     		InternalTestHelper.setInternalUserNumber(100000);
     *
     *
     *     These tests can be modified to suit new solutions, just as long as the performance metrics
     *     at the end of the tests remains consistent.
     *
     *     These are performance metrics that we are trying to hit:
     *
     *     highVolumeTrackLocation: 100,000 users within 15 minutes:
     *     		assertTrue(TimeUnit.MINUTES.toSeconds(15) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
     *
     *     highVolumeGetRewards: 100,000 users within 20 minutes:
     *          assertTrue(TimeUnit.MINUTES.toSeconds(20) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
     */

    @Test
    public void highVolumeTrackLocation() throws InterruptedException {
        // Users should be incremented up to 100,000, and test finishes within 15 minutes
        InternalTestHelper.setInternalUserNumber(100000);
        StopWatch stopWatch = new StopWatch();
        TourGuideService tourGuideService = new TourGuideService(gpsUtilServiceImpl, rewardsServiceImpl);

        List<User> allUsers = tourGuideService.getAllUsers();

        /*
            TrackLocation is supposed to add a new VisitedLocation for each user.
            So VisitedLocation count is saved for each user, and will be compared to new count after tracking.
        */
        List<Integer> initialVisitedLocationsCount = allUsers.stream()
                .map(u -> u.getVisitedLocations().size())
                .collect(Collectors.toList());

        stopWatch.start();
        CompletableFuture<?>[] futures = allUsers.stream()
                .map(tourGuideService.tracker::trackUserLocation)
                .toArray(CompletableFuture[]::new);
        CompletableFuture.allOf(futures).join();
        stopWatch.stop();

        List<Integer> newVisitedLocationsCount = allUsers.stream()
                .map(u -> u.getVisitedLocations().size())
                .collect(Collectors.toList());

        // Comparison of VisitedLocations count
        for (int i = 0; i < initialVisitedLocationsCount.size(); i++) {
            assertEquals(initialVisitedLocationsCount.get(i) + 1, (int) newVisitedLocationsCount.get(i));
        }

        System.out.println("highVolumeTrackLocation: Time Elapsed: " + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()) + " seconds.");
        assertTrue(TimeUnit.MINUTES.toSeconds(15) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
    }

    @Test
    public void highVolumeGetRewards() throws InterruptedException {
        // Users should be incremented up to 100,000, and test finishes within 20 minutes
        InternalTestHelper.setInternalUserNumber(100000);
        StopWatch stopWatch = new StopWatch();

        TourGuideService tourGuideService = new TourGuideService(gpsUtilServiceImpl, rewardsServiceImpl);
        List<AttractionDTO> attractions = gpsUtilServiceImpl.getAttractions();
        List<User> allUsers = tourGuideService.getAllUsers();

        allUsers.forEach(u ->
                u.addToVisitedLocations(new VisitedLocationDTO(u.getUserId(), attractions.get(0), new Date()))
        );

        stopWatch.start();
        CompletableFuture<?>[] futures = allUsers.stream()
                .map(rewardsServiceImpl::calculateRewards)
                .toArray(CompletableFuture[]::new);
        CompletableFuture.allOf(futures).join();
        stopWatch.stop();

        System.out.println("highVolumeGetRewards: Time Elapsed: " + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()) + " seconds.");

        for (User user : allUsers) {
            assertTrue(user.getUserRewards().size() > 0);
        }

        assertTrue(TimeUnit.MINUTES.toSeconds(20) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
    }

}
