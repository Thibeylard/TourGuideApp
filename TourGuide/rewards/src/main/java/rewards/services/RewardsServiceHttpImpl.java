package rewards.services;


import common.dtos.GetDistanceDTO;
import common.dtos.GetRewardPointsDTO;
import common.dtos.UserDTO;
import common.dtos.WithinAttractionProximityDTO;
import common.models.localization.Attraction;
import common.models.localization.Location;
import common.models.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Service
public class RewardsServiceHttpImpl implements RewardsService {
    private RestTemplate restTemplate;

    @Autowired
    public RewardsServiceHttpImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public void updateAttractions() {

    }

    public void setProximityBuffer(int proximityBuffer) {
        String params = "?proximityBuffer=" + proximityBuffer;
        restTemplate.exchange(
                "http://localhost:8080/rewards/setProximityBuffer" + params,
                HttpMethod.PUT,
                null,
                Void.class
        );
    }

    public void setDefaultProximityBuffer() {
        restTemplate.exchange(
                "http://localhost:8080/rewards/setDefaultProximityBuffer",
                HttpMethod.PUT,
                null,
                Void.class
        );
    }

    public CompletableFuture<?> calculateRewards(User user) {
        return CompletableFuture.runAsync(() -> {
            UserDTO dto = new UserDTO(user);
            try {
                dto = restTemplate.exchange(
                        new RequestEntity<>(user, HttpMethod.POST, new URI("http://localhost:8080/rewards/calculateRewards")),
                        UserDTO.class
                ).getBody();
            } catch (URISyntaxException e) {
                // TODO Handle exception
                e.printStackTrace();
            }
            // RewardsServiceHttpImpl doit redéfinir les récompenses utilisateurs qui ont été faite sur le dto dans le RewardsServiceImpl distant
            user.setUserRewards(dto.getUserRewards());
        });
    }

    public boolean isWithinAttractionProximity(Attraction attraction, Location location) {
        try {
            return Objects.requireNonNull(restTemplate.exchange(
                    new RequestEntity<>(new WithinAttractionProximityDTO(attraction, location), HttpMethod.POST, new URI("http://localhost:8080/rewards/isWithinAttractionProximity")),
                    Boolean.class
            ).getBody());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        // TODO Handle exception
        return false;
    }

    public int getRewardPoints(Attraction attraction, User user) {
        try {
            return Objects.requireNonNull(restTemplate.exchange(
                    new RequestEntity<>(new GetRewardPointsDTO(new UserDTO(user), attraction), HttpMethod.POST, new URI("http://localhost:8080/rewards/getRewardPoints")),
                    Integer.class
            ).getBody());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        // TODO Handle exception
        return -1;
    }

    public double getDistance(Location loc1, Location loc2) {
        try {
            return Objects.requireNonNull(restTemplate.exchange(
                    new RequestEntity<>(new GetDistanceDTO(loc1, loc2), HttpMethod.POST, new URI("http://localhost:8080/rewards/getDistance")),
                    Double.class
            ).getBody());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        // TODO Handle exception
        return -1.0;
    }
}