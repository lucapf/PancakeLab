package org.pancakelab.model;

import java.util.Objects;
import java.util.UUID;

public class Order {
    private final UUID id;
    private final int building;
    private final int room;

    private Order(UUID id, int building, int room) {
        this.id = id;
        this.building = building;
        this.room = room;
    }

    Order(int building, int room) {
        this.id = UUID.randomUUID();
        this.building = building;
        this.room = room;
    }

    static Order cloneOrder(Order o) {
        return new Order(o.getId(), o.getBuilding(), o.getRoom());
    }

    public static Order of(int building, int room) {
        return new Order(building, room);
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
}
