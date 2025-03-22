package org.pancakelab.model;

import org.pancakelab.Utils;

import java.util.*;

public class Order {
    private final UUID id;
    private final int building;
    private final int room;
    private final List<Pancake> pancakes;
    private final Step step;

    private Order(Order.Builder builder) {
        this.id = builder.id;
        this.building = builder.building;
        this.room = builder.room;
        this.pancakes = builder.pancakes;
        this.step = builder.step;
    }

    public List<Pancake> getPancakes() {
        return this.pancakes;
    }


    public UUID getId() {
        return id;
    }

    public int getBuilding() {
        return building;
    }

    public int getRoom() {
        return room;
    }

    public Step getStep() {
        return step;
    }

    public Order addPancakes(List<Pancake> pancakes) {
        var o = new Order.Builder(this)
                .setListPancakes(Utils.concat(this.pancakes, pancakes)).build();
        assert o.isPresent(); //cannot be false because was already there
        return o.get();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    public static class Builder {
        UUID id;
        int building;
        int room;
        Step step;
        List<Pancake> pancakes;

        public Builder(Order order) {
            this.id = order.getId();
            this.room = order.getRoom();
            this.building = order.getBuilding();
            this.step = order.step;
            this.pancakes = order.pancakes;
        }

        public Builder(int building, int room) {
            this.id = UUID.randomUUID();
            this.building = building;
            this.room = room;
            this.pancakes = Collections.emptyList();
            this.step = Step.INCOMPLETE;
        }

        public Builder setStep(Step step) {
            this.step = step;
            return this;
        }

        public Builder setListPancakes(List<Pancake> listPancakes) {
            this.pancakes = listPancakes;
            return this;
        }

        public Builder addPancakes(List<Pancake> pancakesToBeAdded) {
            this.pancakes = Utils.concat(this.pancakes, pancakesToBeAdded);
            return this;
        }

        public Optional<Order> build() {
            return this.building < 0 || this.room < 0 ? Optional.empty() : Optional.of(new Order(this));
        }
    }
}
