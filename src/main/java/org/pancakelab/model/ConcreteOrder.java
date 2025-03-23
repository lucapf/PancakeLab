package org.pancakelab.model;

import org.pancakelab.service.Utils;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class ConcreteOrder implements Order {
    private final UUID id;
    private final int building;
    private final int room;
    private final List<Pancake> pancakes;
    private final Step step;
    private final String description;

    ConcreteOrder(Order.Builder builder) {
        this.id = builder.id;
        this.building = builder.building;
        this.room = builder.room;
        this.pancakes = builder.pancakes;
        this.step = builder.step;
        this.description = builder.description;
    }

    @Override
    public List<Pancake> getPancakes() {
        return this.pancakes;
    }


    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public int getBuilding() {
        return building;
    }

    @Override
    public int getRoom() {
        return room;
    }

    @Override
    public Step getStep() {
        return step;
    }

    @Override
    public Order addPancakes(List<Pancake> pancakes) {
        return new ConcreteOrder.Builder(this)
                .setListPancakes(Utils.concat(this.pancakes, pancakes)).build();
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public Type getType() {
        return Type.CONCRETE;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConcreteOrder order = (ConcreteOrder) o;
        return Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

}
