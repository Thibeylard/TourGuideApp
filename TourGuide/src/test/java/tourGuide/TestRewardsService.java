package tourGuide;

import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import tourGuide.helper.InternalTestHelper;
import tourGuide.service.RewardsService;
import tourGuide.service.TourGuideService;
import tourGuide.user.User;
import tourGuide.user.UserReward;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestRewardsService {

	@Autowired
	private GpsUtil gpsUtil;
	@Autowired
	private RewardsService rewardsService;

	@Test
	public void userGetRewards() {

		InternalTestHelper.setInternalUserNumber(0);
		TourGuideService tourGuideService = new TourGuideService(gpsUtil, rewardsService);

		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		Attraction attraction = gpsUtil.getAttractions().get(0);
		user.addToVisitedLocations(new VisitedLocation(user.getUserId(), attraction, new Date()));
		List<User> users = new ArrayList<>();
		users.add(user);
		rewardsService.rewardAndWait(users);
		List<UserReward> userRewards = user.getUserRewards();
		assertEquals(1, userRewards.size());
	}

	@Test
	public void isWithinAttractionProximity() {
		Attraction attraction = gpsUtil.getAttractions().get(0);
		assertTrue(rewardsService.isWithinAttractionProximity(attraction, attraction));
	}

	@Test
	// FIXED : Problem came from tracker who started automatically and also tried to calculateRewards for User.
	public void nearAllAttractions() {
		rewardsService.setProximityBuffer(Integer.MAX_VALUE);

		InternalTestHelper.setInternalUserNumber(1);
		TourGuideService tourGuideService = new TourGuideService(gpsUtil, rewardsService);

		rewardsService.rewardAndWait(tourGuideService.getAllUsers());

		rewardsService.setDefaultProximityBuffer();

		List<UserReward> userRewards = tourGuideService.getUserRewards(tourGuideService.getAllUsers().get(0));

		assertEquals(gpsUtil.getAttractions().size(), userRewards.size());
	}

}
