package common.dtos;

import common.models.localization.Attraction;

public class GetRewardPointsDTO {
    private UserDTO user;
    private Attraction attraction;

    public GetRewardPointsDTO() {
    }

    public GetRewardPointsDTO(UserDTO user, Attraction attraction) {
        this.user = user;
        this.attraction = attraction;
    }

    public UserDTO getUser() {
        return user;
    }

    public Attraction getAttraction() {
        return attraction;
    }
}
