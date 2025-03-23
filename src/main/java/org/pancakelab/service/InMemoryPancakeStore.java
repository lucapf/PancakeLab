package org.pancakelab.service;

import org.pancakelab.model.ConcreteOrder;
import org.pancakelab.model.Order;
import org.pancakelab.model.Pancake;
import org.pancakelab.model.Step;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.IntStream;

/**
 * Store the order status in memory.
 * not designed to be extended.
 */
final class InMemoryPancakeStore implements PancakeStore {
    /**
     * an order to be delivered goes through following steps:
     * incomplete: customer might modify the order
     * completed:  customer completed the order and goes to the chicken
     * prepared: the order is ready to be shipped
     * delivered: customer got the order
     */

    private static final Map<UUID, Order> orders = new ConcurrentHashMap<>();


    @Override
    public Order createOrder(int building, int room) {
        var createdOrder = new ConcreteOrder.Builder(building, room).build();
        orders.put(createdOrder.getId(), createdOrder);
        return createdOrder;
    }

    @Override
    public Order findOrder(UUID orderId) {
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
     * @param orderId  : the order id
     * @param pancake: the Recipe to remove
     * @param count:   items to remove
     */
    @Override
    public Order addPancake(UUID orderId, Pancake pancake, int count) {
        var existingOrder = findOrder(orderId);
        var additionalPancakes =
                IntStream.range(0, count).boxed().map(x -> pancake).toList();
        var updatedOrder = existingOrder.addPancakes(additionalPancakes);
        orders.put(orderId, updatedOrder);
        return existingOrder;
    }


    @Override
    public Order removePancake(Order order, Pancake pancake, int count) {
        var updatedOrder = Utils.removeItem(order, pancake, count);
        orders.put(order.getId(), updatedOrder);
        return updatedOrder;
    }

    @Override
    public Order cancelOrder(UUID orderId) {
        var order = findOrder(orderId);
        orders.remove(orderId);
        return order;
    }


    @Override
    public List<UUID> listCompletedOrders() {
        return filterByStatus(Step.COMPLETED).stream().map(Order::getId).toList();
    }


    @Override
    public List<UUID> listPreparedOrders() {
        return filterByStatus(Step.PREPARED).stream().map(Order::getId).toList();
    }


    private List<Order> filterByStatus(Step step) {
        return orders.values().stream().filter(o -> o.getStep() == step).toList();
    }

    public Order moveOrder(UUID orderId, Step currentStep, Step newStep) {
        var o = findOrder(orderId);
        var newOrder = new ConcreteOrder.Builder(o).setStep(newStep).build();
        orders.put(orderId, newOrder);
        return newOrder;
    }
}
