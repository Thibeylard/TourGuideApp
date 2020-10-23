package rewards.services;

import common.models.localization.Attraction;
import common.models.localization.Location;
import common.models.localization.VisitedLocation;
import common.models.user.User;
import gps.services.GpsUtilService;
import gps.services.GpsUtilServiceImpl;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class RewardsServiceHttpImplTest {

    @Autowired
    private RewardsServiceHttpImpl rewardsServiceHttpImpl;

    @Autowired
    private RewardsServiceImpl rewardsServiceImpl;

    @MockBean
    private GpsUtilService gpsUtilService;

    @Test
    public void Given_rewardsService_When_instantiated_Then_canModifyProximityBuffer() {
        rewardsServiceHttpImpl.setProximityBuffer(rewardsServiceImpl.getDefaultProximityBuffer() + 6);
        assertThat(rewardsServiceImpl.getProximityBuffer())
                .isNotEqualTo(rewardsServiceImpl.getDefaultProximityBuffer())
                .isEqualTo(rewardsServiceImpl.getDefaultProximityBuffer() + 6);

        rewardsServiceHttpImpl.setDefaultProximityBuffer();
        assertThat(rewardsServiceImpl.getProximityBuffer())
                .isNotEqualTo(rewardsServiceImpl.getDefaultProximityBuffer() + 6)
                .isEqualTo(rewardsServiceImpl.getDefaultProximityBuffer());
    }

    @Test
    @Ignore
    public void Given_rewardsService_When_instantiated_Then_returnCompletableFuture() {
        List<Attraction> attractions = new GpsUtilServiceImpl().getAttractions();

        doReturn(attractions).when(gpsUtilService).getAttractions();
        rewardsServiceImpl.updateAttractions();

        User user = new User(UUID.randomUUID(), "user", "339 447 852", "user@mail.com");
        user.addToVisitedLocations(new VisitedLocation(user.getUserId(), attractions.get(0), Date.from(Instant.now())));
        assertThat(rewardsServiceHttpImpl.calculateRewards(user))
                .isNotNull()
                .isInstanceOf(CompletableFuture.class);
    }

    @Test
    public void Given_rewardsService_When_instantiated_Then_isWithinAttractionProximityReturnValidBoolean() {
        Location location = new Location(-12.458965, 45.984134);
        Attraction attraction = new Attraction("attraction", "city", "state", location.latitude, location.longitude);
        assertThat(rewardsServiceHttpImpl.isWithinAttractionProximity(attraction, location))
                .isTrue();
    }

    @Test
    public void Given_rewardsService_When_instantiated_Then_getRewardPointsIsGreaterThanZero() {
        Location location = new Location(-12.458965, 45.984134);
        User user = new User(UUID.randomUUID(), "user", "339 447 852", "user@mail.com");
        user.addToVisitedLocations(new VisitedLocation(user.getUserId(), location, Date.from(Instant.now())));
        Attraction attraction = new Attraction("attraction", "city", "state", location.latitude, location.longitude);
        assertThat(rewardsServiceHttpImpl.getRewardPoints(attraction, user) > 0)
                .isTrue();
    }

    @Test
    public void Given_rewardsService_When_instantiated_Then_getDistanceReturnValidDouble() {
        Location location = new Location(-12.458965, 45.984134);
        Location location1 = new Location(12.458965, -45.984134);
        assertThat(rewardsServiceHttpImpl.getDistance(location, location1) > 0.0)
                .isTrue();
    }
}
