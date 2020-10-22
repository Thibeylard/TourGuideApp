package common.dtos;

import common.models.user.UserPreferences;

public class UserPreferencesDTO {
    private String username;
    private int attractionProximity;
    private int lowerPricePoint;
    private int highPricePoint;
    private int tripDuration;
    private int ticketQuantity;
    private int numberOfAdults;
    private int numberOfChildren;

    private UserPreferencesDTO() {
    }

    public UserPreferencesDTO(String username,
                              int attractionProximity,
                              int lowerPricePoint,
                              int highPricePoint,
                              int tripDuration,
                              int ticketQuantity,
                              int numberOfAdults,
                              int numberOfChildren) {
        this.username = username;
        this.attractionProximity = attractionProximity;
        this.lowerPricePoint = lowerPricePoint;
        this.highPricePoint = highPricePoint;
        this.tripDuration = tripDuration;
        this.ticketQuantity = ticketQuantity;
        this.numberOfAdults = numberOfAdults;
        this.numberOfChildren = numberOfChildren;
    }

    public UserPreferencesDTO(String username, UserPreferences userPreferences) {
        this.username = username;
        this.attractionProximity = userPreferences.getAttractionProximity();
        this.lowerPricePoint = userPreferences.getLowerPricePoint().getNumber().intValueExact();
        this.highPricePoint = userPreferences.getHighPricePoint().getNumber().intValueExact();
        this.tripDuration = userPreferences.getTripDuration();
        this.ticketQuantity = userPreferences.getTicketQuantity();
        this.numberOfAdults = userPreferences.getNumberOfAdults();
        this.numberOfChildren = userPreferences.getNumberOfChildren();
    }

    public String getUsername() {
        return username;
    }

    public int getAttractionProximity() {
        return attractionProximity;
    }

    public int getLowerPricePoint() {
        return lowerPricePoint;
    }

    public int getHighPricePoint() {
        return highPricePoint;
    }

    public int getTripDuration() {
        return tripDuration;
    }

    public int getTicketQuantity() {
        return ticketQuantity;
    }

    public int getNumberOfAdults() {
        return numberOfAdults;
    }

    public int getNumberOfChildren() {
        return numberOfChildren;
    }
}
