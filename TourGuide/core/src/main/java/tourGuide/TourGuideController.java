package tourGuide;

import com.jsoniter.output.JsonStream;
import gpsUtil.location.VisitedLocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tourGuide.dto.UserPreferencesDTO;
import tourGuide.service.TourGuideService;
import tourGuide.user.User;
import tripPricer.Provider;

import java.util.List;

@RestController
public class TourGuideController {

    TourGuideService tourGuideService;

    @Autowired
    public TourGuideController(TourGuideService tourGuideService) {
        this.tourGuideService = tourGuideService;
    }

    @RequestMapping("/")
    public String index() {
        return "Greetings from TourGuide!";
    }

    //TODO secure endpoint
    @RequestMapping("/admin/action/startTracker")
    public void startTracker() {
        tourGuideService.tracker.startTracking();
    }

    // TODO secure endpoint
    @RequestMapping("/admin/action/stopTracker")
    public void stopTracker() {
        tourGuideService.tracker.stopTracking();
    }

    // TODO secure endpoint
    @RequestMapping("/admin/action/getAllUsersLocations")
    public String getAllCurrentLocations() {
        return JsonStream.serialize(tourGuideService.getAllUsersLastLocation());
    }

    @RequestMapping("/user/getLocation")
    public String getLocation(@RequestParam String userName) {
        VisitedLocation visitedLocation = tourGuideService.getUserLocation(getUser(userName));
        return JsonStream.serialize(visitedLocation.location);
    }

    @RequestMapping("/user/getNearbyAttractions")
    public String getNearbyAttractions(@RequestParam String userName) {
        return JsonStream.serialize(tourGuideService.getUserAttractionRecommendation(userName));
    }

    @RequestMapping("/user/getRewards")
    public String getRewards(@RequestParam String userName) {
        return JsonStream.serialize(tourGuideService.getUserRewards(getUser(userName)));
    }

    @RequestMapping("/user/getTripDeals")
    public String getTripDeals(@RequestParam String userName) {
        List<Provider> providers = tourGuideService.getTripDeals(getUser(userName));
        return JsonStream.serialize(providers);
    }

    @PutMapping("/user/updatePreferences")
    public String updatePreferences(@RequestBody UserPreferencesDTO preferencesUpdate) {
        return JsonStream.serialize(
                new UserPreferencesDTO(preferencesUpdate.getUsername(),
                        tourGuideService.updateUserPreferences(preferencesUpdate))
        );
    }

    private User getUser(String userName) {
        return tourGuideService.getUser(userName);
    }


}