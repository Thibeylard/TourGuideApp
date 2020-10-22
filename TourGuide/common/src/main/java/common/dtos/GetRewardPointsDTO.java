package common.dtos;

import common.models.localization.Attraction;
import common.models.user.User;

public class GetRewardPointsDTO {
    private User user;
    private Attraction attraction;

    public GetRewardPointsDTO() {
    }

    public GetRewardPointsDTO(User user, Attraction attraction) {
        this.user = user;
        this.attraction = attraction;
    }

    public User getUser() {
        return user;
    }

    public Attraction getAttraction() {
        return attraction;
    }
}
