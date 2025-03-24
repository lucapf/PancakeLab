package org.pancakelab.service;

import org.pancakelab.model.ConcreteOrder;
import org.pancakelab.model.Order;
import org.pancakelab.model.OrderStatus;
import org.pancakelab.model.Pancake;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Store the order status in memory.
 * not designed to be extended.
 */
final class InMemoryPancakeStore implements PancakeStore {

    private static final Map<UUID, Order> orders = new ConcurrentHashMap<>();

    @Override
    public Order createOrder(int building, int room) {
        var createdOrder = new ConcreteOrder.Builder(building, room).build();
        putValue(createdOrder);
        return createdOrder;
    }

    @Override
    public Order findOrderById(UUID orderId) {
        var o = Optional.ofNullable(orders.get(
                Objects.requireNonNullElse(orderId, UUID.randomUUID())
        ));
        return o.orElseGet(() -> Order.Builder.buildNull("order with id: %s does not exists".formatted(orderId)));
    }

    @Override
    public Order findOrderByIdAndStatus(UUID orderId, OrderStatus orderStatus) {
        var order = findOrderById(orderId);
        return order.getStatus() == orderStatus
                ? order
                : Order.Builder.buildNull("order id: %s with status: %s not found".formatted(orderId, orderStatus));
    }

    /**
     * add new pancakes to an existing order.
     * Only incomplete order are modifiable!
     *
     * @param existingOrder: the order we are going to update
     * @param pancakes:      list of pancake to add
     */
    @Override
    public Order addPancakes(Order existingOrder, List<Pancake> pancakes) {
        var updatedOrder = existingOrder.addPancakes(pancakes);
        putValue(updatedOrder);
        return updatedOrder;
    }


    @Override
    public Order removePancakes(Order order, List<Pancake> pancakes) {
        var updatedOrder = order.removePancakes(pancakes);
        putValue(updatedOrder);
        return updatedOrder;
    }

    @Override
    public void deleteOrder(Order order) {
        orders.remove(order.getId());
    }


    @Override
    public List<Order> findOrdersByStatus(OrderStatus orderStatus) {
        return orders.values().stream().filter(o -> o.getStatus() == orderStatus).toList();
    }

    @Override
    public Order moveOrder(Order existingOrder, OrderStatus newOrderStatus) {
        var newOrder = new Order.Builder(existingOrder).setStatus(newOrderStatus).build();
        putValue(newOrder);
        return newOrder;
    }

    /**
     * store concrete orders only
     */
    private void putValue(Order order) {
        if (order instanceof ConcreteOrder)
            orders.put(order.getId(), order);
    }

}
