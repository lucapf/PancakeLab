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
        enum Type{
            VALID, INVALID;
        }
        UUID id;
        int building;
        int room;
        OrderStatus orderStatus;
        List<Pancake> pancakes;
        String description;
        Type type;

        public Builder() {
            this(0, 0);
            type = Type.INVALID;
        }

        public Builder(Order order) {
            this(order.getBuilding(), order.getRoom());
            this.id = order.getId();
            this.building = order.getBuilding();
            this.orderStatus = order.getStatus();
            this.pancakes = order.getPancakes();
        }

        public Builder(int building, int room) {
            this.id = UUID.randomUUID();
            this.building = building;
            this.room = room;
            this.pancakes = Collections.emptyList();
            this.orderStatus = OrderStatus.INCOMPLETE;
            this.description = checkAddress();
        }

        private String checkAddress() {
            if (building <= 0 || room <= 0){
                this.type = Type.INVALID;
                return "the address is not invalid";
            }
            this.type = Type.VALID;
            return "building: %d room: %d".formatted(this.building, this.room);
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder setStep(OrderStatus orderStatus) {
            this.orderStatus = orderStatus;
            return this;
        }

        public Builder setListPancakes(List<Pancake> listPancakes) {
            this.pancakes = listPancakes;
            return this;
        }

        public Order build() {
            return type==Type.INVALID? new NullOrder("the address is not valid")
                    : new ConcreteOrder(this);
        }
    }
}
