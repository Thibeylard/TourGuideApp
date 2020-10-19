package tourGuide.integration;

import gps.GpsUtilServiceImpl;
import models.dto.AttractionDTO;
import models.dto.VisitedLocationDTO;
import models.user.User;
import models.user.UserReward;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import rewards.RewardsServiceImpl;
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
	private GpsUtilServiceImpl gpsUtilServiceImpl;
	@Autowired
	private RewardsServiceImpl rewardsServiceImpl;

	@Test
	public void userGetRewards() {

		InternalTestHelper.setInternalUserNumber(0);
		TourGuideService tourGuideService = new TourGuideService(gpsUtilServiceImpl, rewardsServiceImpl);

		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		AttractionDTO attraction = gpsUtilServiceImpl.getAttractions().get(0);
		user.addToVisitedLocations(new VisitedLocationDTO(user.getUserId(), attraction, new Date()));

		rewardsServiceImpl.calculateRewards(user).join();

		List<UserReward> userRewards = user.getUserRewards();
		assertEquals(1, userRewards.size());
	}

	@Test
	public void isWithinAttractionProximity() {
		AttractionDTO attraction = gpsUtilServiceImpl.getAttractions().get(0);
		assertTrue(rewardsServiceImpl.isWithinAttractionProximity(attraction, attraction));
	}

	@Test
	// FIXED : Problem came from tracker who started automatically and also tried to calculateRewards for User.
	public void nearAllAttractions() {
		rewardsServiceImpl.setProximityBuffer(Integer.MAX_VALUE);

		InternalTestHelper.setInternalUserNumber(1);
		TourGuideService tourGuideService = new TourGuideService(gpsUtilServiceImpl, rewardsServiceImpl);

		CompletableFuture<?>[] futures = tourGuideService.getAllUsers().stream()
				.map(rewardsServiceImpl::calculateRewards)
				.toArray(CompletableFuture[]::new);
		CompletableFuture.allOf(futures).join();

		rewardsServiceImpl.setDefaultProximityBuffer();

		List<UserReward> userRewards = tourGuideService.getUserRewards(tourGuideService.getAllUsers().get(0));

		assertEquals(gpsUtilServiceImpl.getAttractions().size(), userRewards.size());
	}

}
