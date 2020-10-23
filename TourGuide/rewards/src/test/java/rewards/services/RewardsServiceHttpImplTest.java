package rewards.services;

import common.models.localization.Attraction;
import common.models.localization.Location;
import common.models.user.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class RewardsServiceHttpImplTest {

    @Autowired
    private RewardsServiceHttpImpl rewardsServiceHttpImpl;
    @Autowired
    private RewardsServiceImpl rewardsServiceImpl;


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
    public void Given_rewardsService_When_instantiated_Then_returnCompletableFuture() {
        User user = new User(UUID.randomUUID(), "user", "339 447 852", "user@mail.com");
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
        User user = new User(UUID.randomUUID(), "user", "339 447 852", "user@mail.com");
        Location location = new Location(-12.458965, 45.984134);
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
