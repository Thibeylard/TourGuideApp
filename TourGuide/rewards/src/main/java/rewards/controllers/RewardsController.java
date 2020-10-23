package rewards.controllers;

import common.dtos.GetDistanceDTO;
import common.dtos.GetRewardPointsDTO;
import common.dtos.WithinAttractionProximityDTO;
import common.models.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import rewards.services.RewardsService;

import java.util.concurrent.CompletableFuture;

@RestController
public class RewardsController {

    private final RewardsService rewardsService;

    @Autowired
    public RewardsController(@Qualifier("rewardsServiceImpl") RewardsService rewardsService) {
        this.rewardsService = rewardsService;
    }

    @PutMapping("rewards/setProximityBuffer")
    public void setProximityBuffer(int proximityBuffer) {
        rewardsService.setProximityBuffer(proximityBuffer);
    }

    @PutMapping("rewards/setDefaultProximityBuffer")
    public void setDefaultProximityBuffer() {
        rewardsService.setDefaultProximityBuffer();
    }

    @GetMapping("rewards/calculateRewards")
    public CompletableFuture<?> calculateRewards(@RequestBody User user) {
        return rewardsService.calculateRewards(user);
    }

    @GetMapping("rewards/isWithinAttractionProximity")
    public boolean isWithinAttractionProximity(@RequestBody WithinAttractionProximityDTO dto) {
        return rewardsService.isWithinAttractionProximity(dto.getAttraction(), dto.getLocation());
    }

    @GetMapping("rewards/getRewardPoints")
    public int getRewardPoints(@RequestBody GetRewardPointsDTO dto) {
        return rewardsService.getRewardPoints(dto.getAttraction(), dto.getUser());
    }

    @GetMapping("rewards/getDistance")
    public double getDistance(@RequestBody GetDistanceDTO dto) {
        return rewardsService.getDistance(dto.getFirstLocation(), dto.getSecondLocation());
    }


}