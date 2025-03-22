package org.pancakelab;

import org.pancakelab.model.Order;
import org.pancakelab.model.Pancake;
import org.pancakelab.model.Step;

import java.util.*;
import java.util.stream.IntStream;

/**
 * Store the order status in memory.
 * not designed to be extended.
 */
public final class InMemoryPancakeStore implements PancakeStore {
    /**
     * an order to be delivered goes through following steps:
     * incomplete: customer might modify the order
     * completed:  customer completed the order and goes to the chicken
     * prepared: the order is ready to be shipped
     * delivered: customer got the order
     */

    private static final Map<UUID, Order> orders = new HashMap<>();


    @Override
    public Optional<Order> createOrder(int building, int room) {
        var createdOrder = new Order.Builder(building, room).build();
        createdOrder.ifPresent(order -> orders.put(order.getId(), order));
        return createdOrder;
    }

    @Override
    public Optional<Order> findOrder(UUID orderId) {
        Objects.requireNonNull(orderId);
        return Optional.ofNullable(orders.get(orderId));
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
    public Optional<Order> addPancake(UUID orderId, Pancake pancake, int count) {
        var existingOrder = findOrder(orderId);
        if (existingOrder.isEmpty()) return Optional.empty();
        var additionalPancakes =
                IntStream.range(0, count).boxed().map(x -> pancake).toList();
        var updatedOrder = existingOrder.get().addPancakes(additionalPancakes);
        orders.put(orderId, updatedOrder);
        return existingOrder;
    }

    @Override
    public List<String> viewOrder(UUID uuid) {
        return findOrder(uuid).stream()
                .map(Order::getPancakes).flatMap(Collection::stream)
                .map(Pancake::getName)
                .toList();
    }

    @Override
    public Order removePancake(Order order, Pancake pancake, int count) {
        var updatedOrder = Utils.removeItem(order, pancake, count);
        orders.put(order.getId(), updatedOrder);
        return updatedOrder;
    }

    @Override
    public Optional<Order> cancelOrder(UUID orderId) {
        var o = Optional.ofNullable(orders.get(orderId));
        orders.remove(orderId);
        return o;
    }

    @Override
    public void completeOrder(UUID orderId) {
        move(orderId, Step.INCOMPLETE, Step.COMPLETED);

    }

    @Override
    public void preparedOrder(UUID orderId) {
        move(orderId, Step.COMPLETED, Step.PREPARED);
    }


    @Override
    public List<UUID> listCompletedOrders() {
        return filterByStatus(Step.COMPLETED).stream().map(Order::getId).toList();
    }


    @Override
    public List<UUID> listPreparedOrders() {
        return filterByStatus(Step.PREPARED).stream().map(Order::getId).toList();
    }

    @Override
    public Optional<Order> deliverOrder(UUID orderId) {
        return move(orderId, Step.PREPARED, Step.DELIVERED);
    }

    private List<Order> filterByStatus(Step step) {
        return orders.values().stream().filter(o -> o.getStep() == step).toList();
    }

    private Optional<Order> move(UUID orderId, Step currentStep, Step newStep) {
        var o = Optional.ofNullable(orders.get(orderId));
        if (o.isPresent() && o.get().getStep() == currentStep) {
            var newOptionalOrder = new Order.Builder(o.get()).setStep(newStep).build();
            assert newOptionalOrder.isPresent();
            var newOrder = newOptionalOrder.get();
            orders.put(newOrder.getId(), newOrder);
            return Optional.of(newOrder);
        }
        return o;
    }
}
