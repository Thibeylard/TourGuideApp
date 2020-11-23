package gps.unit;

import common.models.localization.Attraction;
import common.services.GpsUtilService;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class GpsUtilServiceImplTest {

    @Autowired
    private GpsUtilService gpsUtilService;

    @Test
    public void Given_rewardsService_When_instantiated_Then_getUserLocationReturnVisitedLocation() {
        UUID userId = new UUID(4872158, 1875147);
        Assertions.assertThat(gpsUtilService.getUserLocation(userId))
                .isNotNull()
                .hasFieldOrPropertyWithValue("userId", userId)
                .hasFieldOrProperty("location")
                .hasFieldOrProperty("timeVisited");
    }

    @Test
    public void Given_rewardsService_When_instantiated_Then_attractionListIsNotNullNorEmpty() {
        List<Attraction> attractionList = gpsUtilService.getAttractions();
        Assertions.assertThat(attractionList)
                .isNotNull()
                .isNotEmpty();
    }
}
