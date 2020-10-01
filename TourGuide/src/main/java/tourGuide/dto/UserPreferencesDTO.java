package tourGuide.dto;

import org.javamoney.moneta.Money;

public class UserPreferencesDTO {
    private final String username;
    private final int attractionProximity;
    private final Money lowerPricePoint;
    private final Money highPricePoint;
    private final int tripDuration;
    private final int ticketQuantity;
    private final int numberOfAdults;
    private final int numberOfChildren;

    public UserPreferencesDTO(String username,
                              int attractionProximity,
                              Money lowerPricePoint,
                              Money highPricePoint,
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

    public String getUsername() {
        return username;
    }

    public int getAttractionProximity() {
        return attractionProximity;
    }

    public Money getLowerPricePoint() {
        return lowerPricePoint;
    }

    public Money getHighPricePoint() {
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
