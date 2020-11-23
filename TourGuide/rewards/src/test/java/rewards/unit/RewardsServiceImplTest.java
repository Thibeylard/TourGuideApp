package rewards.unit;

import common.models.localization.Attraction;
import common.models.localization.Location;
import common.models.localization.VisitedLocation;
import common.models.user.User;
import common.services.GpsUtilService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import rewards.services.RewardsServiceImpl;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class RewardsServiceImplTest {

	@Autowired
	private RewardsServiceImpl rewardsServiceImpl;
	@Autowired
	private GpsUtilService gpsUtilServiceImpl;

	@Test
	public void modifyingProximityBuffer() {
		rewardsServiceImpl.setProximityBuffer(rewardsServiceImpl.getDefaultProximityBuffer() + 6);
		assertThat(rewardsServiceImpl.getProximityBuffer())
				.isNotEqualTo(rewardsServiceImpl.getDefaultProximityBuffer())
				.isEqualTo(rewardsServiceImpl.getDefaultProximityBuffer() + 6);

		rewardsServiceImpl.setDefaultProximityBuffer();
		assertThat(rewardsServiceImpl.getProximityBuffer())
				.isNotEqualTo(rewardsServiceImpl.getDefaultProximityBuffer() + 6)
				.isEqualTo(rewardsServiceImpl.getDefaultProximityBuffer());
	}

	@Test
	public void isWithinAttractionProximity() {
		Attraction attraction = gpsUtilServiceImpl.getAttractions().get(0);
		assertTrue(rewardsServiceImpl.isWithinAttractionProximity(attraction, attraction));
	}

	@Test
	public void calculateRewards() throws ExecutionException, InterruptedException {
		List<Attraction> attractions = gpsUtilServiceImpl.getAttractions();

		User user = new User(UUID.randomUUID(), "user", "339 447 852", "user@mail.com");
		user.addToVisitedLocations(new VisitedLocation(user.getUserId(), attractions.get(0), Date.from(Instant.now())));

		assertThat(user.getUserRewards())
				.hasSize(0);

		CompletableFuture<?> answer = rewardsServiceImpl.calculateRewards(user);
		assertThat(answer)
				.isNotNull()
				.isInstanceOf(CompletableFuture.class);

		assertThat(answer.get())
				.isNotNull()
				.isInstanceOf(User.class);

		assertThat(user.getUserRewards())
				.hasSize(1);
	}

	@Test
	public void getRewardPoints() {
		Location location = new Location(45.984134, -12.458965);
		User user = new User(UUID.randomUUID(), "user", "339 447 852", "user@mail.com");
		user.addToVisitedLocations(new VisitedLocation(user.getUserId(), location, Date.from(Instant.now())));
		Attraction attraction = new Attraction("attraction", "city", "state", location.latitude, location.longitude);
		assertThat(rewardsServiceImpl.getRewardPoints(attraction, user) > 0)
				.isTrue();
	}

	@Test
	public void getDistance() {
		Location location = new Location(45.984134, -12.458965);
		Location location1 = new Location(-45.984134, 12.458965);
		assertThat(rewardsServiceImpl.getDistance(location, location1) > 0.0)
				.isTrue();
	}

}
