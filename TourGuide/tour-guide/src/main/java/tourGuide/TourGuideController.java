package tourGuide;

import com.jsoniter.output.JsonStream;
import common.dtos.UserPreferencesDTO;
import common.models.localization.VisitedLocation;
import common.models.marketing.Provider;
import common.models.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tourGuide.services.TourGuideService;

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
    @PostMapping("/admin/action/startTracker")
    public void startTracker() {
        tourGuideService.tracker.startTracking();
    }

    // TODO secure endpoint
    @PostMapping("/admin/action/stopTracker")
    public void stopTracker() {
        tourGuideService.tracker.stopTracking();
    }

    // TODO secure endpoint
    @GetMapping("/admin/action/getAllUsersLocations")
    public String getAllCurrentLocations() {
        return JsonStream.serialize(tourGuideService.getAllUsersLastLocation());
    }

    @GetMapping("/user/getLocation")
    public String getLocation(@RequestParam String userName) {
        VisitedLocation visitedLocation = tourGuideService.getUserLocation(getUser(userName));
        return JsonStream.serialize(visitedLocation.location);
    }

    @GetMapping("/user/getNearbyAttractions")
    public String getNearbyAttractions(@RequestParam String userName) {
        return JsonStream.serialize(tourGuideService.getUserAttractionRecommendation(userName));
    }

    @GetMapping("/user/getRewards")
    public String getRewards(@RequestParam String userName) {
        return JsonStream.serialize(tourGuideService.getUserRewards(getUser(userName)));
    }

    @GetMapping("/user/getTripDeals")
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