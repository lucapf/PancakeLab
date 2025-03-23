package org.pancakelab.model;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public interface Order {
    List<Pancake> getPancakes();

    UUID getId();

    int getBuilding();

    int getRoom();

    Step getStep();

    Order addPancakes(List<Pancake> pancakes);

    String getDescription();

    Type getType();

    enum Type {CONCRETE, INVALID}

    class Builder {
        UUID id;
        int building;
        int room;
        Step step;
        List<Pancake> pancakes;
        String description;
        Type type;

        public Builder() {
            this(0, 0);
        }

        public Builder(Order order) {
            this(order.getBuilding(), order.getRoom());
            this.id = order.getId();
            this.building = order.getBuilding();
            this.step = order.getStep();
            this.pancakes = order.getPancakes();
        }
        private Type checkAddress(int building, int room){
           return building <= 0 || room<=0?Type.INVALID:Type.CONCRETE;
        }
        public Builder(int building, int room) {
            this.type = checkAddress(building, room);
            this.id = UUID.randomUUID();
            this.building = building;
            this.room = room;
            this.pancakes = Collections.emptyList();
            this.step = Step.INCOMPLETE;
            this.description = type == Type.CONCRETE
                                ?"building: %d room: %d".formatted(this.building, this.room)
                                :"the address is not invalid";
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder setStep(Step step) {
            this.step = step;
            return this;
        }

        public Builder setListPancakes(List<Pancake> listPancakes) {
            this.pancakes = listPancakes;
            return this;
        }

        public Order build() {
            return this.building < 0 || this.room < 0
                    ? new NullOrder("the address is not valid")
                    : new ConcreteOrder(this);
        }
    }
}
