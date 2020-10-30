package common.models.localization;

import java.util.UUID;

public class Attraction extends Location {
    public String attractionName;
    public String city;
    public String state;
    public UUID attractionId;


    public Attraction() {
    }

    public Attraction(String attractionName, String city, String state, double latitude, double longitude) {
        super(longitude, latitude);
        this.attractionName = attractionName;
        this.city = city;
        this.state = state;
        this.attractionId = UUID.randomUUID();
    }

    public Attraction(double latitude, double longitude) {
        super(longitude, latitude);
    }

    public String getAttractionName() {
        return attractionName;
    }

    public void setAttractionName(String attractionName) {
        this.attractionName = attractionName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

/*    public UUID getAttractionId() {
        return attractionId;
    }

    public void setAttractionId(UUID attractionId) {
        this.attractionId = attractionId;
    }*/
}