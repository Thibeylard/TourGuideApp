package tourGuide.service;

import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;
import org.javamoney.moneta.Money;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tourGuide.dto.NearbyAttraction;
import tourGuide.dto.UserAttractionRecommendation;
import tourGuide.helper.InternalTestHelper;
import tourGuide.tracker.Tracker;
import tourGuide.user.User;
import tourGuide.user.UserPreferences;
import tourGuide.user.UserReward;
import tripPricer.Provider;
import tripPricer.TripPricer;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.IntStream;

@Service
public class TourGuideService {
	private Logger logger = LoggerFactory.getLogger(TourGuideService.class);
	private final GpsUtil gpsUtil;
	private final RewardsService rewardsService;
	private final TripPricer tripPricer = new TripPricer();
	public final Tracker tracker;
	boolean testMode = true;

	@Autowired
	public TourGuideService(GpsUtil gpsUtil, RewardsService rewardsService) {
		this.gpsUtil = gpsUtil;
		this.rewardsService = rewardsService;

		if (testMode) {
			logger.info("TestMode enabled");
			logger.debug("Initializing users");
			initializeInternalUsers();
			logger.debug("Finished initializing users");
		}
		this.tracker = new Tracker(this, gpsUtil, rewardsService);
		addShutDownHook();
	}

	public List<UserReward> getUserRewards(User user) {
		return user.getUserRewards();
	}

	public VisitedLocation getUserLocation(User user) {
		VisitedLocation visitedLocation;

		if (user.getVisitedLocations().size() > 0) {
			visitedLocation = user.getLastVisitedLocation();
		} else {
			// Very rare case of location initiation, which is why gpsUtil can be invoke directly
			visitedLocation = gpsUtil.getUserLocation(user.getUserId());
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

	public HashMap<String, Location> getAllUsersLastLocation() {
		HashMap<String, Location> usersLastLocation = new HashMap<>();
		getAllUsers()
				.forEach(u -> usersLastLocation.put(u.getLastVisitedLocation().userId.toString(), u.getLastVisitedLocation().location));
		return usersLastLocation;
	}

	public void addUser(User user) {
		if (!internalUserMap.containsKey(user.getUserName())) {
			internalUserMap.put(user.getUserName(), user);
		}
	}

	public List<Provider> getTripDeals(User user) {
		int cumulativeRewardPoints = user.getUserRewards()
				.stream().mapToInt(UserReward::getRewardPoints).sum();
		List<Provider> providers = tripPricer.getPrice(
				tripPricerApiKey,
				user.getUserId(),
				user.getUserPreferences().getNumberOfAdults(),
				user.getUserPreferences().getNumberOfChildren(),
				user.getUserPreferences().getTripDuration(),
				cumulativeRewardPoints);
		user.setTripDeals(providers);
		return providers;
	}

	public UserAttractionRecommendation getUserAttractionRecommendation(String username) {
		VisitedLocation userLastLocation = getUser(username).getLastVisitedLocation();
		List<Attraction> nearbyAttractions = getNearByAttractions(userLastLocation);
		Map<String, NearbyAttraction> nearbyAttractionHashMap = new HashMap<>();
		nearbyAttractions.forEach(att ->
				nearbyAttractionHashMap.put(
						att.attractionName,
						new NearbyAttraction(
								att.latitude,
								att.longitude,
								rewardsService.getDistance(att, userLastLocation.location),
								rewardsService.getRewardPoints(att, getUser(username))))
		);
		return new UserAttractionRecommendation(userLastLocation.location, nearbyAttractionHashMap);
	}

	public List<Attraction> getNearByAttractions(VisitedLocation visitedLocation) {
		List<Attraction> nearbyAttractions = new ArrayList<>();
		Attraction farthestAttraction;
		for (Attraction attraction : gpsUtil.getAttractions()) {
			if (nearbyAttractions.size() == 5) {
				farthestAttraction = nearbyAttractions.stream()
						.max(Comparator.comparing(
								attraction1 -> rewardsService.getDistance(visitedLocation.location, attraction1)))
						.get();

				if (rewardsService.getDistance(attraction, visitedLocation.location) < rewardsService.getDistance(farthestAttraction, visitedLocation.location)) {
					nearbyAttractions.add(attraction);
					nearbyAttractions.remove(farthestAttraction);
				}
			} else {
				nearbyAttractions.add(attraction);
			}
		}

		return nearbyAttractions;
	}

	/**********************************************************************************
	 *
	 * Methods Below: For Internal Testing
	 *
	 **********************************************************************************/
	private static final String tripPricerApiKey = "test-server-api-key";

	public UserPreferences updateUserPreferences(String username,
												 int attractionProximity,
												 Money lowerPricePoint,
												 Money highPricePoint,
												 int tripDuration,
												 int ticketQuantity,
												 int numberOfAdults,
												 int numberOfChildren) {
		User user = getUser(username);
		UserPreferences updates = new UserPreferences(attractionProximity,
				lowerPricePoint,
				highPricePoint,
				tripDuration,
				ticketQuantity,
				numberOfAdults,
				numberOfChildren);
		user.setUserPreferences(updates);

		return updates;
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
			user.addToVisitedLocations(new VisitedLocation(user.getUserId(), new Location(generateRandomLatitude(), generateRandomLongitude()), getRandomTime()));
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
