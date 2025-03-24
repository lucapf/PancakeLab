package org.pancakelab.model;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public interface Order {
    List<Pancake> getPancakes();

    UUID getId();

    int getBuilding();

    int getRoom();

    OrderStatus getStatus();

    Order addPancakes(List<Pancake> pancakes);

    Order removePancakes(List<Pancake> pancakes);

    String getDescription();

    class Builder {
        final int building;
        final int room;
        UUID id;
        OrderStatus orderStatus;
        List<Pancake> pancakes;
        String description;
        Type type;

        public Builder(Order order) {
            this(order.getBuilding(), order.getRoom());
            this.id = order.getId();
            this.type = order instanceof NullOrder ? Type.NULL : Type.CONCRETE;
            this.orderStatus = order.getStatus();
            this.pancakes = order.getPancakes();
            this.description = order.getDescription();
        }

        public Builder(int building, int room) {
            checkAddress(building, room);
            this.id = UUID.randomUUID();
            this.building = building;
            this.room = room;
            this.pancakes = Collections.emptyList();
            this.orderStatus = OrderStatus.INCOMPLETE;
            this.description = Type.CONCRETE == this.type
                    ? "building: %d room: %d".formatted(this.building, this.room)
                    : "the address building: %d room: %d is not valid".formatted(this.building, this.room);
        }

        public static Order buildNull(String description) {
            return new NullOrder(description);
        }

        private void checkAddress(int building, int room) {
            this.type = building > 0 && room > 0 ? Type.CONCRETE : Type.NULL;
        }

        public Builder setStatus(OrderStatus orderStatus) {
            this.orderStatus = orderStatus;
            return this;
        }

        public Builder setListPancakes(List<Pancake> listPancakes) {
            this.pancakes = listPancakes;
            return this;
        }

        public Order build() {
            return this.type == Type.CONCRETE
                    ? new ConcreteOrder(this)
                    : new NullOrder(this.description);
        }

        enum Type {NULL, CONCRETE}
    }
}
