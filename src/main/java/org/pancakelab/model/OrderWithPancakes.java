package org.pancakelab.model;

import org.pancakelab.Utils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class OrderWithPancakes {
    private final Order order;
    private final List<PancakesRecipe> pancakes;

    private OrderWithPancakes(Order order) {
        this.order = Order.cloneOrder(order);
        this.pancakes = Collections.emptyList();
    }

    private OrderWithPancakes(Order order, List<PancakesRecipe> pancakes) {
        //enforce immutability
        this.order = Order.cloneOrder(order);
        this.pancakes = List.copyOf(pancakes);
    }

    public static OrderWithPancakes of(Order order, List<PancakesRecipe> pancakes) {
        return new OrderWithPancakes(order, pancakes);
    }

    public static OrderWithPancakes of(Order order) {
        return new OrderWithPancakes(order, Collections.emptyList());
    }

    public static OrderWithPancakes addPancakes(OrderWithPancakes orderWithPancakes, List<PancakesRecipe> additionalPancakes) {
        return new OrderWithPancakes(orderWithPancakes.getOrder(),
                Utils.concat(orderWithPancakes.getPancakes(), additionalPancakes));
    }

    public UUID getOrderId() {
        return UUID.fromString(this.order.getId().toString());
    }

    public Order getOrder() {
        return Order.cloneOrder(this.order);
    }

    public List<PancakesRecipe> getPancakes() {
        return List.copyOf(pancakes);
    }

    public List<String> getPancakesNames() {
        return pancakes.stream().map(PancakesRecipe::name).toList();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof OrderWithPancakes that)) return false;
        return Objects.equals(order, that.order);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(order);
    }
}