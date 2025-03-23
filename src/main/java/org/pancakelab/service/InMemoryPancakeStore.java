package org.pancakelab.service;

import org.pancakelab.model.*;

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
        putValue( createdOrder);
        return createdOrder;
    }

    @Override
    public Order findOrderById(UUID orderId) {
        var o = Optional.ofNullable(orders.get(
                Objects.requireNonNullElse(orderId, UUID.randomUUID())
        ));
        return o.orElseGet(() -> new Order.Builder()
                .setDescription("order with id: %s does not exists".formatted(orderId))
                .build());
    }

    /**
     * add new pancakes to an existing order.
     * Only incomplete order are modifiable!
     *
     * @param orderId   : the order id
     * @param pancakes: list of pancake to add
     */
    @Override
    public Order addPancakes(UUID orderId,OrderStatus orderStatus , List<Pancake> pancakes) {
        var existingOrder = findOrderByIdAndStatus(orderId, orderStatus);
        var updatedOrder = existingOrder.addPancakes(pancakes);
        putValue(updatedOrder);
        return existingOrder;
    }


    @Override
    public Order removePancakes(Order order, List<Pancake> pancakes) {
        var updatedOrder = order.removePancakes(pancakes);
        putValue(updatedOrder);
        return updatedOrder;
    }

    @Override
    public Order deleteOrder(UUID orderId) {
        var order = findOrderById(orderId);
        orders.remove(orderId);
        return order;
    }


    @Override
    public List<Order> findOrdersByStatus(OrderStatus orderStatus) {
        return orders.values().stream().filter(o -> o.getStatus() == orderStatus).toList();
    }

    @Override
    public Order moveOrder(UUID orderId, OrderStatus currentOrderStatus, OrderStatus newOrderStatus) {
        var existingOrder = findOrderByIdAndStatus(orderId, currentOrderStatus);
        var newOrder = new Order.Builder(existingOrder).setStep(newOrderStatus).build();
        putValue(newOrder);
        return newOrder;
    }

    /**
     * store concrete orders only
     */
    private void putValue(Order order){
        if (order instanceof  ConcreteOrder)
                orders.put(order.getId(), order);
    }
   private Order findOrderByIdAndStatus(UUID orderId, OrderStatus orderStatus) {
       var o = findOrderById(orderId);
       return o.getStatus() == orderStatus?o
               : new Order.Builder().setDescription(
               "order id: %s with status: %s not found".formatted(orderId, orderStatus)).build();
   }
}
