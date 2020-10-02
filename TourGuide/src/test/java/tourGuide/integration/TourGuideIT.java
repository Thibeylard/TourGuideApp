package tourGuide.integration;

import com.jsoniter.JsonIterator;
import com.jsoniter.any.Any;
import com.jsoniter.output.JsonStream;
import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import tourGuide.dto.UserPreferencesDTO;
import tourGuide.service.TourGuideService;
import tourGuide.user.User;
import tourGuide.user.UserPreferences;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TourGuideIT {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;
    @Autowired
    private TourGuideService tourGuideService;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .build();
    }

    @Test
    public void trackerEndpoints() throws Exception {
        assertThat(tourGuideService.tracker.isStopped()) // At start, tracker is disabled
                .isTrue();

        User someUser = tourGuideService.getAllUsers().get(0);
        VisitedLocation previousLocation = someUser.getLastVisitedLocation();

        TimeUnit.SECONDS.sleep(1);

        assertThat(someUser.getLastVisitedLocation()) // Still not tracked.
                .usingRecursiveComparison()
                .isEqualTo(previousLocation);

        mockMvc.perform(get("/admin/action/startTracker"));
        assertThat(tourGuideService.tracker.isStopped())
                .isFalse();

        TimeUnit.SECONDS.sleep(1); // 1 second sleep is enough for 100 users to be located

        assertThat(someUser.getLastVisitedLocation()) // Users have been successfully tracked
                .usingRecursiveComparison()
                .isNotEqualTo(previousLocation);

        mockMvc.perform(get("/admin/action/stopTracker"));

        assertThat(tourGuideService.tracker.isStopped())
                .isTrue();
    }

    @Test
    public void allUsersLocation() throws Exception {
        MvcResult result = mockMvc.perform(get("/admin/action/getAllUsersLocations"))
                .andReturn();

        String json = result.getResponse().getContentAsString();

        Any usersLocations = JsonIterator.deserialize(json);

        usersLocations.asMap().forEach((userId, location) -> {
            Optional<User> user = tourGuideService.getAllUsers().stream()
                    .filter(u -> u.getUserId().equals(UUID.fromString(userId)))
                    .findFirst();

            assertThat(user)
                    .isNotEmpty();

            Location userLastLocation = user.get().getLastVisitedLocation().location;

            // Because double was used for longitude and latitude, rounding errors. Use of BigDecimal with specific scale to workaround the problem
            assertThat(location.toBigDecimal("latitude").setScale(6, RoundingMode.HALF_UP).doubleValue())
                    .isEqualTo(BigDecimal.valueOf(userLastLocation.latitude).setScale(6, RoundingMode.HALF_UP).doubleValue());
            assertThat(location.toBigDecimal("longitude").setScale(6, RoundingMode.HALF_UP).doubleValue())
                    .isEqualTo(BigDecimal.valueOf(userLastLocation.longitude).setScale(6, RoundingMode.HALF_UP).doubleValue());

        });
    }

    @Test
    public void updateUserPreferences() throws Exception {
        User someUser = tourGuideService.getAllUsers().get(10);

        UserPreferencesDTO oldPreferences = new UserPreferencesDTO(
                someUser.getUserName(),
                1500,
                1000,
                2000,
                15,
                3,
                2,
                1);

        someUser.setUserPreferences(new UserPreferences(oldPreferences));

        assertThat(someUser.getUserPreferences())
                .usingRecursiveComparison()
                .isEqualTo(new UserPreferences(oldPreferences));

        UserPreferencesDTO newPreferences = new UserPreferencesDTO(
                someUser.getUserName(),
                500,
                500,
                1500,
                7,
                2,
                2,
                2
        );


        mockMvc.perform(put("/user/updatePreferences")
                .accept(MediaType.ALL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonStream.serialize(newPreferences)))
                .andExpect(status().isOk());

        assertThat(someUser.getUserPreferences())
                .usingRecursiveComparison()
                .isEqualTo(new UserPreferences(newPreferences));
    }
}
