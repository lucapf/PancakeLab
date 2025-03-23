package org.pancakelab.model;

import java.util.List;
import java.util.UUID;

public class NullOrder implements Order {
    private final String description;

    NullOrder(String description) {
        this.description = description;
    }

    @Override
    public List<Pancake> getPancakes() {
        return List.of();
    }

    @Override
    public UUID getId() {
        // NullOrderIds have always the same UUID
        return UUID.fromString("59b680da-7e26-4f97-ae28-6ae4e890315b");
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
    public Step getStep() {
        return Step.NONE;
    }

    @Override
    public Order addPancakes(List<Pancake> pancakes) {
        return this;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public Type getType() {
        return Type.INVALID;
    }
}
