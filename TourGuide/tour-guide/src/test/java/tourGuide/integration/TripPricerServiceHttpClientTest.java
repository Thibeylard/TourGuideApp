package tourGuide.integration;

import common.models.marketing.Provider;
import common.services.TripPricerService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@RunWith(SpringRunner.class)
@ActiveProfiles("itest")
public class TripPricerServiceHttpClientTest {

    @Autowired
    private TripPricerService tripPricerServiceHttpImpl;

    @Test
    public void getPrice() {
        assertThat(tripPricerServiceHttpImpl.getPrice("test", new UUID(15440, 4558), 2, 4, 6, 43))
                .hasOnlyElementsOfType(Provider.class)
                .hasSize(5);
    }
}
