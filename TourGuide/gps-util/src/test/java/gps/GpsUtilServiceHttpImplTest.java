package gps;

import gps.services.GpsUtilServiceHttpImpl;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

@SpringBootTest
@RunWith(SpringRunner.class)
public class GpsUtilServiceHttpImplTest {

    @Autowired
    private GpsUtilServiceHttpImpl gpsUtilServiceHttp;

    @Test
    public void Given_rewardsService_When_instantiated_Then_getUserLocationReturnVisitedLocation() {
        UUID userId = new UUID(4872158, 1875147);
        Assertions.assertThat(gpsUtilServiceHttp.getUserLocation(userId))
                .isNotNull()
                .hasFieldOrPropertyWithValue("userId", userId)
                .hasFieldOrProperty("location")
                .hasFieldOrProperty("timeVisited");
    }

    @Test
    public void Given_rewardsService_When_instantiated_Then_attractionListIsNotNullNorEmpty() {
        Assertions.assertThat(gpsUtilServiceHttp.getAttractions())
                .isNotNull()
                .isNotEmpty();
    }
}
