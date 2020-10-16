package tourGuide.service;

import gps.GpsUtilService;
import models.dto.*;
import models.user.User;
import models.user.UserPreferences;
import models.user.UserReward;
import org.javamoney.moneta.Money;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rewards.RewardsService;
import tourGuide.helper.InternalTestHelper;
import tourGuide.tracker.Tracker;
import tripPricer.TripPricerService;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class TourGuideService {
	private Logger logger = LoggerFactory.getLogger(TourGuideService.class);
	private final GpsUtilService gpsUtilService;
	private final RewardsService rewardsService;
	private final TripPricerService tripPricer = new TripPricerService();
	public final Tracker tracker;
	boolean testMode = true;

	@Autowired
	public TourGuideService(GpsUtilService gpsUtilService, RewardsService rewardsService) {
		this.gpsUtilService = gpsUtilService;
		this.rewardsService = rewardsService;

		if (testMode) {
			logger.info("TestMode enabled");
			logger.debug("Initializing users");
			initializeInternalUsers();
			logger.debug("Finished initializing users");
		}
		this.tracker = new Tracker(this, gpsUtilService, rewardsService);
		addShutDownHook();
	}

	public List<UserReward> getUserRewards(User user) {
		return user.getUserRewards();
	}

	public VisitedLocationDTO getUserLocation(User user) {
		VisitedLocationDTO visitedLocation;

		if (user.getVisitedLocations().size() > 0) {
			visitedLocation = user.getLastVisitedLocation();
		} else {
			// Very rare case of location initiation, which is why gpsUtil can be invoke directly
			visitedLocation = gpsUtilService.getUserLocation(user.getUserId());
			user.addToVisitedLocations(visitedLocation);
		}
		return visitedLocation;
	}

	public User getUser(String userName) {
		return internalUserMap.get(userName);
	}

	public List<User> getAllUsers() {
		return new ArrayList<>(internalUserMap.values());
	}

	public HashMap<String, LocationDTO> getAllUsersLastLocation() {
		HashMap<String, LocationDTO> usersLastLocation = new HashMap<>();
		getAllUsers()
				.forEach(u -> usersLastLocation.put(u.getLastVisitedLocation().userId.toString(), u.getLastVisitedLocation().location));
		return usersLastLocation;
	}

	public void addUser(User user) {
		if (!internalUserMap.containsKey(user.getUserName())) {
			internalUserMap.put(user.getUserName(), user);
		}
	}

	public List<ProviderDTO> getTripDeals(User user) {
		int cumulativeRewardPoints = user.getUserRewards()
				.stream().mapToInt(UserReward::getRewardPoints).sum();
		List<ProviderDTO> providers = tripPricer.getPrice(
				tripPricerApiKey,
				user.getUserId(),
				user.getUserPreferences().getNumberOfAdults(),
				user.getUserPreferences().getNumberOfChildren(),
				user.getUserPreferences().getTripDuration(),
				cumulativeRewardPoints);
		user.setTripDeals(providers);
		return providers;
	}

	public AttractionRecommendationDTO getUserAttractionRecommendation(String username) {
		VisitedLocationDTO userLastLocation = getUser(username).getLastVisitedLocation();
		List<AttractionDTO> nearbyAttractions = getNearByAttractions(userLastLocation);
		Map<String, NearbyAttractionDTO> nearbyAttractionHashMap = new HashMap<>();
		nearbyAttractions.forEach(att ->
				nearbyAttractionHashMap.put(
						att.attractionName,
						new NearbyAttractionDTO(
								att.latitude,
								att.longitude,
								rewardsService.getDistance(att, userLastLocation.location),
								rewardsService.getRewardPoints(att, getUser(username))))
		);
		return new AttractionRecommendationDTO(userLastLocation.location, nearbyAttractionHashMap);
	}

	public List<AttractionDTO> getNearByAttractions(VisitedLocationDTO visitedLocation) {
		List<AttractionDTO> nearbyAttractions = gpsUtilService.getAttractions();
		nearbyAttractions = nearbyAttractions.stream()
				.sorted(Comparator.comparing(
						attraction -> rewardsService.getDistance(visitedLocation.location, attraction)))
				.limit(5)
				.collect(Collectors.toList());

		return nearbyAttractions;
	}

	/**********************************************************************************
	 *
	 * Methods Below: For Internal Testing
	 *
	 **********************************************************************************/
	private static final String tripPricerApiKey = "test-server-api-key";

	public UserPreferences updateUserPreferences(UserPreferencesDTO preferencesUpdate) {
		User user = getUser(preferencesUpdate.getUsername());
		user.setUserPreferences(new UserPreferences(preferencesUpdate));

		return user.getUserPreferences();
	}

	private void addShutDownHook() {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				if (!testMode) {
					tracker.stopTracking();
				}
			}
		});
	}
	// Database connection will be used for external users, but for testing purposes internal users are provided and stored in memory
	private final Map<String, User> internalUserMap = new HashMap<>();
	private void initializeInternalUsers() {
		IntStream.range(0, InternalTestHelper.getInternalUserNumber()).forEach(i -> {
			String userName = "internalUser" + i;
			String phone = "000";
			String email = userName + "@tourGuide.com";
			User user = new User(UUID.randomUUID(), userName, phone, email);
			generateUserLocationHistory(user);
			generateUserPreferences(user);

			internalUserMap.put(userName, user);
		});
		logger.debug("Created " + InternalTestHelper.getInternalUserNumber() + " internal test users.");
	}

	private void generateUserPreferences(User user) {
		UserPreferences userPreferences = new UserPreferences();
		userPreferences.setNumberOfAdults(new Random().nextInt(3) + 1);
		userPreferences.setNumberOfChildren(new Random().nextInt(5));
		userPreferences.setLowerPricePoint(Money.of(new Random().nextInt(1000), userPreferences.getCurrency()));
		userPreferences.setHighPricePoint(Money.of(new Random().nextInt(5000), userPreferences.getCurrency()).add(userPreferences.getLowerPricePoint()));
		userPreferences.setTripDuration(new Random().nextInt(14) + 1);
		userPreferences.setTicketQuantity(userPreferences.getNumberOfAdults() + userPreferences.getNumberOfChildren() / 2);
		user.setUserPreferences(userPreferences);
	}

	private void generateUserLocationHistory(User user) {
		IntStream.range(0, 3).forEach(i -> {
			user.addToVisitedLocations(new VisitedLocationDTO(user.getUserId(), new LocationDTO(generateRandomLatitude(), generateRandomLongitude()), getRandomTime()));
		});
	}

	private double generateRandomLongitude() {
		double leftLimit = -180;
		double rightLimit = 180;
	    return leftLimit + new Random().nextDouble() * (rightLimit - leftLimit);
	}

	private double generateRandomLatitude() {
		double leftLimit = -85.05112878;
	    double rightLimit = 85.05112878;
	    return leftLimit + new Random().nextDouble() * (rightLimit - leftLimit);
	}

	private Date getRandomTime() {
		LocalDateTime localDateTime = LocalDateTime.now().minusDays(new Random().nextInt(30));
	    return Date.from(localDateTime.toInstant(ZoneOffset.UTC));
	}

}
