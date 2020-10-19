package rewards.controllers;

import models.dto.AttractionDTO;
import models.dto.LocationDTO;
import models.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import rewards.services.RewardsService;

import java.util.concurrent.CompletableFuture;

@RestController
public class RewardsController {

    private final RewardsService rewardsService;

    @Autowired
    public RewardsController(RewardsService rewardsService) {
        this.rewardsService = rewardsService;
    }

    @GetMapping("rewards/setProximityBuffer")
    public void setProximityBuffer(int proximityBuffer) {
        rewardsService.setProximityBuffer(proximityBuffer);
    }

    ;

    @GetMapping("rewards/setDefaultProximityBuffer")
    public void setDefaultProximityBuffer() {
        rewardsService.setDefaultProximityBuffer();
    }

    ;

    @GetMapping("rewards/calculateRewards")
    public CompletableFuture<?> calculateRewards(User user) {
        return rewardsService.calculateRewards(user);
    }

    ;

    @GetMapping("rewards/isWithinAttractionProximity")
    public boolean isWithinAttractionProximity(AttractionDTO attraction, LocationDTO location) {
        return rewardsService.isWithinAttractionProximity(attraction, location);
    }

    ;

    @GetMapping("rewards/getRewardPoints")
    public int getRewardPoints(AttractionDTO attraction, User user) {
        return rewardsService.getRewardPoints(attraction, user);
    }

    ;

    @GetMapping("rewards/getDistance")
    public double getDistance(LocationDTO loc1, LocationDTO loc2) {
        return rewardsService.getDistance(loc1, loc2);
    }

    ;


}