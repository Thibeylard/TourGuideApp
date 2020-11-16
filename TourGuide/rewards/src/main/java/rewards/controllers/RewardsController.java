package rewards.controllers;

import common.dtos.GetDistanceDTO;
import common.dtos.GetRewardPointsDTO;
import common.dtos.UserDTO;
import common.dtos.WithinAttractionProximityDTO;
import common.models.user.User;
import common.services.RewardsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

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

    @PostMapping("rewards/calculateRewards")
    public UserDTO calculateRewards(@RequestBody UserDTO dto) throws ExecutionException, InterruptedException {
        return new UserDTO((User) rewardsService.calculateRewards(new User(dto)).get());
    }

    @PostMapping("rewards/isWithinAttractionProximity")
    public boolean isWithinAttractionProximity(@RequestBody WithinAttractionProximityDTO dto) {
        return rewardsService.isWithinAttractionProximity(dto.getAttraction(), dto.getLocation());
    }

    @PostMapping("rewards/getRewardPoints")
    public int getRewardPoints(@RequestBody GetRewardPointsDTO dto) {
        return rewardsService.getRewardPoints(dto.getAttraction(), new User(dto.getUser()));
    }

    @PostMapping("rewards/getDistance")
    public double getDistance(@RequestBody GetDistanceDTO dto) {
        return rewardsService.getDistance(dto.getFirstLocation(), dto.getSecondLocation());
    }


}