package common.dtos;


import common.models.localization.VisitedLocation;
import common.models.user.User;
import common.models.user.UserReward;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class UserDTO {
    private UUID userId;
    private String userName;
    private String phoneNumber;
    private String emailAddress;
    private Date latestLocationTimestamp;
    private List<VisitedLocation> visitedLocations = new ArrayList<>();
    private List<UserReward> userRewards = new ArrayList<>();

    public UserDTO() {
    }

    public UserDTO(User user) {
        this.userId = user.getUserId();
        this.userName = user.getUserName();
        this.phoneNumber = user.getPhoneNumber();
        this.emailAddress = user.getEmailAddress();
        this.latestLocationTimestamp = user.getLatestLocationTimestamp();
        this.visitedLocations = user.getVisitedLocations();
        this.userRewards = user.getUserRewards();
    }

    public UUID getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public Date getLatestLocationTimestamp() {
        return latestLocationTimestamp;
    }

    public List<VisitedLocation> getVisitedLocations() {
        return visitedLocations;
    }

    public List<UserReward> getUserRewards() {
        return userRewards;
    }
}
