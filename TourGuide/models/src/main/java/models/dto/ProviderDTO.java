package models.dto;

import java.util.UUID;

public class ProviderDTO {
    public final String name;
    public final double price;
    public final UUID tripId;

    public ProviderDTO(UUID tripId, String name, double price) {
        this.name = name;
        this.tripId = tripId;
        this.price = price;
    }
}
