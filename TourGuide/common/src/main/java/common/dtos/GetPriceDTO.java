package common.dtos;

import common.models.marketing.Provider;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GetPriceDTO {
    private String apiKey;
    private UUID attractionId;
    private int adults;
    private int children;
    private int nightsStay;
    private int rewardsPoints;

    private List<Provider> providers = new ArrayList<>();

    public GetPriceDTO(String apiKey, UUID attractionId, int adults, int children, int nightsStay, int rewardsPoints) {
        this.apiKey = apiKey;
        this.attractionId = attractionId;
        this.adults = adults;
        this.children = children;
        this.nightsStay = nightsStay;
        this.rewardsPoints = rewardsPoints;
    }

    public GetPriceDTO() {
    }

    public String getApiKey() {
        return apiKey;
    }

    public UUID getAttractionId() {
        return attractionId;
    }

    public int getAdults() {
        return adults;
    }

    public int getChildren() {
        return children;
    }

    public int getNightsStay() {
        return nightsStay;
    }

    public int getRewardsPoints() {
        return rewardsPoints;
    }

    public List<Provider> getProviders() {
        return providers;
    }

    public GetPriceDTO withProviders(List<Provider> providers) {
        this.providers = providers;
        return this;
    }
}
