package tourGuide.integration;

import common.models.localization.Attraction;
import common.models.localization.VisitedLocation;
import common.models.user.User;
import common.models.user.UserReward;
import gps.services.GpsUtilService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import rewards.services.RewardsService;
import tourGuide.helper.InternalTestHelper;
import tourGuide.service.TourGuideService;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RewardsServiceImplIT {

	@Autowired
	private GpsUtilService gpsUtilService;
	@Autowired
	private RewardsService rewardsService;

	@Test
	public void userGetRewards() {

		InternalTestHelper.setInternalUserNumber(0);
		TourGuideService tourGuideService = new TourGuideService(gpsUtilService, rewardsService);

		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		Attraction attraction = gpsUtilService.getAttractions().get(0);
		user.addToVisitedLocations(new VisitedLocation(user.getUserId(), attraction, new Date()));

		rewardsService.calculateRewards(user).join();

		List<UserReward> userRewards = user.getUserRewards();
		assertEquals(1, userRewards.size());
	}

	@Test
	public void isWithinAttractionProximity() {
		Attraction attraction = gpsUtilService.getAttractions().get(0);
		assertTrue(rewardsService.isWithinAttractionProximity(attraction, attraction));
	}

	@Test
	// FIXED : Problem came from tracker who started automatically and also tried to calculateRewards for User.
	public void nearAllAttractions() {
		rewardsService.setProximityBuffer(Integer.MAX_VALUE);

		InternalTestHelper.setInternalUserNumber(1);
		TourGuideService tourGuideService = new TourGuideService(gpsUtilService, rewardsService);

		CompletableFuture<?>[] futures = tourGuideService.getAllUsers().stream()
				.map(rewardsService::calculateRewards)
				.toArray(CompletableFuture[]::new);
		CompletableFuture.allOf(futures).join();

		rewardsService.setDefaultProximityBuffer();

		List<UserReward> userRewards = tourGuideService.getUserRewards(tourGuideService.getAllUsers().get(0));

		assertEquals(gpsUtilService.getAttractions().size(), userRewards.size());
	}

}
