package org.pancakelab.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;

public class ConcreteOrder implements Order {
    private final UUID id;
    private final int building;
    private final int room;
    private final List<Pancake> pancakes;
    private final OrderStatus orderStatus;
    private final String description;

    ConcreteOrder(Order.Builder builder) {
        this.id = builder.id;
        this.building = builder.building;
        this.room = builder.room;
        this.pancakes = builder.pancakes;
        this.orderStatus = builder.orderStatus;
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
    public OrderStatus getStatus() {
        return orderStatus;
    }

    @Override
    public Order addPancakes(List<Pancake> pancakes) {
        return new Order.Builder(this)
                .setListPancakes(
                        Stream.concat(this.pancakes.stream(), pancakes.stream()).toList()
                ).build();
    }

    @Override
    public Order removePancakes(List<Pancake> pancakes) {
        var existingPancakes = new ArrayList<>(this.pancakes);
        pancakes.forEach( existingPancakes::remove);
        return  new Order.Builder(this).setListPancakes(existingPancakes).build();
    }

    @Override
    public String getDescription() {
        return description;
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
