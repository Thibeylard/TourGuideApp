package tourGuide.integration;

import common.models.localization.Attraction;
import common.models.localization.Location;
import common.models.localization.VisitedLocation;
import common.models.user.User;
import gps.services.GpsUtilServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import rewards.services.RewardsService;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class RewardsServiceHttpImplTest {

    @Autowired
    private RewardsService rewardsServiceHttpImpl;

    @Test
    public void calculateRewards() {
        List<Attraction> attractions = new GpsUtilServiceImpl().getAttractions();

        User user = new User(UUID.randomUUID(), "user", "339 447 852", "user@mail.com");
        user.addToVisitedLocations(new VisitedLocation(user.getUserId(), attractions.get(0), Date.from(Instant.now())));

        assertThat(user.getUserRewards())
                .hasSize(0);

        CompletableFuture<?> answer = rewardsServiceHttpImpl.calculateRewards(user);
        assertThat(answer)
                .isNotNull()
                .isInstanceOf(CompletableFuture.class);

        answer.join();

        assertThat(user.getUserRewards())
                .hasSize(1);
    }

    @Test
    public void isWithinAttractionProximity() {
        Location location = new Location(45.984134, -12.458965);
        Attraction attraction = new Attraction("attraction", "city", "state", location.latitude, location.longitude);
        assertThat(rewardsServiceHttpImpl.isWithinAttractionProximity(attraction, location))
                .isTrue();
    }

    @Test
    public void getRewardPoints() {
        Location location = new Location(45.984134, -12.458965);
        User user = new User(UUID.randomUUID(), "user", "339 447 852", "user@mail.com");
        user.addToVisitedLocations(new VisitedLocation(user.getUserId(), location, Date.from(Instant.now())));
        Attraction attraction = new Attraction("attraction", "city", "state", location.latitude, location.longitude);
        assertThat(rewardsServiceHttpImpl.getRewardPoints(attraction, user) > 0)
                .isTrue();
    }

    @Test
    public void getDistance() {
        Location location = new Location(45.984134, -12.458965);
        Location location1 = new Location(-45.984134, 12.458965);
        assertThat(rewardsServiceHttpImpl.getDistance(location, location1) > 0.0)
                .isTrue();
    }
}
