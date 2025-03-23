package org.pancakelab.model;

import java.util.List;
import java.util.UUID;

public class NullOrder implements Order {
    private final String description;

    NullOrder(String description) {
        this.description = description;
    }

    public static UUID getNullID() {
        return UUID.fromString("00000000-0000-0000-0000-000000000000");
    }

    @Override
    public List<Pancake> getPancakes() {
        return List.of();
    }

    @Override
    public UUID getId() {
        // NullOrderIds have always the same UUID
        return getNullID();
    }

    @Override
    public int getBuilding() {
        return 0;
    }

    @Override
    public int getRoom() {
        return 0;
    }

    @Override
    public OrderStatus getStatus() {
        return OrderStatus.NONE;
    }

    @Override
    public Order addPancakes(List<Pancake> pancakes) {
        return this;
    }

    @Override
    public Order removePancakes(List<Pancake> pancakes) {
        return this;
    }

    @Override
    public String getDescription() {
        return description;
    }

}
