package models.dto;

import java.util.UUID;

public class ProviderDTO {
    public String name;
    public double price;
    public UUID tripId;

    public ProviderDTO() {
    }

    public ProviderDTO(UUID tripId, String name, double price) {
        this.name = name;
        this.tripId = tripId;
        this.price = price;
    }
}
