package tourGuide;

import com.jsoniter.output.JsonStream;
import gpsUtil.location.VisitedLocation;
import org.javamoney.moneta.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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

    @RequestMapping("/getLocation")
    public String getLocation(@RequestParam String userName) {
        VisitedLocation visitedLocation = tourGuideService.getUserLocation(getUser(userName));
        return JsonStream.serialize(visitedLocation.location);
    }
    
    @RequestMapping("/getNearbyAttractions")
    public String getNearbyAttractions(@RequestParam String userName) {
        return JsonStream.serialize(tourGuideService.getUserAttractionRecommendation(userName));
    }
    
    @RequestMapping("/getRewards") 
    public String getRewards(@RequestParam String userName) {
    	return JsonStream.serialize(tourGuideService.getUserRewards(getUser(userName)));
    }

    @RequestMapping("/getAllCurrentLocations")
    public String getAllCurrentLocations() {
        return JsonStream.serialize(tourGuideService.getAllUsersLastLocation());
    }

    @RequestMapping("/getTripDeals")
    public String getTripDeals(@RequestParam String userName) {
        List<Provider> providers = tourGuideService.getTripDeals(getUser(userName));
        return JsonStream.serialize(providers);
    }

    @RequestMapping("/updatePreferences")
    public String updatePreferences(@RequestParam String userName,
                                    @RequestParam int attractionProximity,
                                    @RequestParam Money lowerPricePoint,
                                    @RequestParam Money highPricePoint,
                                    @RequestParam int tripDuration,
                                    @RequestParam int ticketQuantity,
                                    @RequestParam int numberOfAdults,
                                    @RequestParam int numberOfChildren) {
        return JsonStream.serialize(
                tourGuideService.updateUserPreferences(userName,
                        attractionProximity,
                        lowerPricePoint,
                        highPricePoint,
                        tripDuration,
                        ticketQuantity,
                        numberOfAdults,
                        numberOfChildren));
    }

    private User getUser(String userName) {
        return tourGuideService.getUser(userName);
    }


}