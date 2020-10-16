package tourGuide.unit;

import com.jsoniter.output.JsonStream;
import gps.GpsUtilService;
import models.dto.*;
import models.user.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import rewards.RewardsService;
import tourGuide.helper.InternalTestHelper;
import tourGuide.service.TourGuideService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestTourGuideService {

    @Autowired
    private GpsUtilService gpsUtilService;
    @Autowired
    private RewardsService rewardsService;

	@Test
	public void getUserLocation() {
        GpsUtilService gpsUtil = new GpsUtilService();
        RewardsService rewardsService = new RewardsService(gpsUtil);
        InternalTestHelper.setInternalUserNumber(0);
        TourGuideService tourGuideService = new TourGuideService(gpsUtil, rewardsService);

        User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
        VisitedLocationDTO visitedLocation = tourGuideService.getUserLocation(user);
        assertEquals(visitedLocation.userId, user.getUserId());
        ;
    }

	@Test
	public void addUser() {
        InternalTestHelper.setInternalUserNumber(0);
        TourGuideService tourGuideService = new TourGuideService(gpsUtilService, rewardsService);

        User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
        User user2 = new User(UUID.randomUUID(), "jon2", "000", "jon2@tourGuide.com");

        tourGuideService.addUser(user);
        tourGuideService.addUser(user2);

        User retrievedUser = tourGuideService.getUser(user.getUserName());
        User retrievedUser2 = tourGuideService.getUser(user2.getUserName());

        assertEquals(user, retrievedUser);
        assertEquals(user2, retrievedUser2);
    }

    @Test
    public void getAllUsers() {
        GpsUtilService gpsUtilService = new GpsUtilService();
        RewardsService rewardsService = new RewardsService(gpsUtilService);
        InternalTestHelper.setInternalUserNumber(0);
        TourGuideService tourGuideService = new TourGuideService(gpsUtilService, rewardsService);

        User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
        User user2 = new User(UUID.randomUUID(), "jon2", "000", "jon2@tourGuide.com");

        tourGuideService.addUser(user);
        tourGuideService.addUser(user2);

        List<User> allUsers = tourGuideService.getAllUsers();

		assertTrue(allUsers.contains(user));
		assertTrue(allUsers.contains(user2));
	}

    @Test
    public void trackUser() {
        GpsUtilService gpsUtilService = new GpsUtilService();
        RewardsService rewardsService = new RewardsService(gpsUtilService);
        InternalTestHelper.setInternalUserNumber(0);
        TourGuideService tourGuideService = new TourGuideService(gpsUtilService, rewardsService);

        User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");

        int visitedLocationCount = user.getVisitedLocations().size();

        tourGuideService.tracker.trackUserLocation(user).join();

        assertEquals(visitedLocationCount + 1, user.getVisitedLocations().size());
    }

	@Test
	public void getAllUserLocations() {
        GpsUtilService gpsUtilService = new GpsUtilService();
        RewardsService rewardsService = new RewardsService(gpsUtilService);
        InternalTestHelper.setInternalUserNumber(100);
        TourGuideService tourGuideService = new TourGuideService(gpsUtilService, rewardsService);

        LinkedHashMap<String, LocationDTO> allUsersLastLocation = new LinkedHashMap<>(tourGuideService.getAllUsersLastLocation());
        String generatedJson = JsonStream.serialize(allUsersLastLocation);

        System.out.println(generatedJson);

        StringBuilder manualJson = new StringBuilder("{");
        allUsersLastLocation.forEach((id, location) ->
                manualJson.append("\"")
                        .append(id)
                        .append("\":")
                        .append("{\"longitude\":").append(BigDecimal.valueOf(location.longitude).setScale(6, RoundingMode.HALF_UP).doubleValue())
						.append(",")
						.append("\"latitude\":").append(BigDecimal.valueOf(location.latitude).setScale(6, RoundingMode.HALF_UP).doubleValue())
						.append("},")
		);
		manualJson.deleteCharAt(manualJson.length() - 1).append("}");

		System.out.println(manualJson.toString());

		assertThat(manualJson.toString())
				.isEqualToIgnoringWhitespace(generatedJson);
	}

	@Test
	public void getNearbyAttractions() {
        GpsUtilService gpsUtilSpy = spy(new GpsUtilService());
        RewardsService rewardsServiceSpy = spy(new RewardsService(gpsUtilSpy));
        InternalTestHelper.setInternalUserNumber(0);
        TourGuideService tourGuideService = new TourGuideService(gpsUtilSpy, rewardsServiceSpy);

        User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
        VisitedLocationDTO visitedLocation = tourGuideService.getUserLocation(user);

        List<AttractionDTO> mockAttractions = new ArrayList<>();
        List<AttractionDTO> closestAttractions = new ArrayList<>();
        AttractionDTO mockAttraction;
        for (int i = 10; i > 0; i--) {
            mockAttraction = mock(AttractionDTO.class);
            doReturn((double) i).when(rewardsServiceSpy).getDistance(mockAttraction, visitedLocation.location);
            doReturn((double) i).when(rewardsServiceSpy).getDistance(visitedLocation.location, mockAttraction);
            mockAttractions.add(mockAttraction);

            if (i < 6) {
                closestAttractions.add(mockAttraction);
            }
        }

        when(gpsUtilSpy.getAttractions()).thenReturn(mockAttractions);

        List<AttractionDTO> attractions = tourGuideService.getNearByAttractions(visitedLocation);

        assertThat(attractions)
                .hasSize(5)
                .containsAll(closestAttractions);
    }

    @Test
    public void getUserAttractionRecommendation() {
        GpsUtilService gpsUtilService = new GpsUtilService();
        RewardsService rewardsService = new RewardsService(gpsUtilService);
        InternalTestHelper.setInternalUserNumber(1);
        TourGuideService tourGuideService = new TourGuideService(gpsUtilService, rewardsService);

        User user = tourGuideService.getAllUsers().get(0);
        UserAttractionRecommendation userAttractionRecommendation =
                tourGuideService.getUserAttractionRecommendation(user.getUserName());

        String generatedJson = JsonStream.serialize(userAttractionRecommendation);

        System.out.println(generatedJson);

        assertThat(userAttractionRecommendation.getUserPosition())
                .isEqualTo(user.getLastVisitedLocation().location);

        assertThat(userAttractionRecommendation.getNearbyAttractions())
                .hasSize(5);

    }

    @Test
    public void getTripDeals() {
        GpsUtilService gpsUtilService = new GpsUtilService();
        RewardsService rewardsService = new RewardsService(gpsUtilService);
        InternalTestHelper.setInternalUserNumber(0);
        TourGuideService tourGuideService = new TourGuideService(gpsUtilService, rewardsService);

        User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");

        List<ProviderDTO> providers = tourGuideService.getTripDeals(user);
        for (ProviderDTO provider :
                providers) {
            System.out.println("--------------- TRIP -------------");
            System.out.println("Name :" + provider.name);
            System.out.println("Price :" + provider.price);
            System.out.println("ID :" + provider.tripId);
            System.out.println("==================================");
        }

        assertEquals(5, providers.size());
    }


}
