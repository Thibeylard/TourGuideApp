package gps;

import gps.services.GpsUtilServiceHttpImpl;
import models.dto.AttractionDTO;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.UUID;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
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
        List<AttractionDTO> attractionDTOList = gpsUtilServiceHttp.getAttractions();
        Assertions.assertThat(attractionDTOList)
                .isNotNull()
                .isNotEmpty();
    }
}
