package org.pancakelab.service;

import org.pancakelab.model.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

/**
 * Pancake expose all the PancakeLab APIs. Is a Singleton
 */
public enum PancakeService {
    INSTANCE;

    private final PancakeStore pancakeStore = new InMemoryPancakeStore();

    /**
     * assuming valid addresses are with positive numbers
     *
     * @param building: positive number indicates the building
     * @param room:     positive number indicates the room
     * @return if address is  valid return the Order. Empty object otherwise
     */
    public synchronized  Order createOrder(int building, int room) {
        return pancakeStore.createOrder(building, room);
    }

    public synchronized void addPancakes(UUID orderId, int count, Ingredient... ingredients) {
        var pancake = new Pancake.Builder(ingredients).build();
        var additionalPancakes = IntStream.range(0, count).boxed().map(x -> pancake).toList();
        var existingOrder = pancakeStore.findOrderByIdAndStatus(orderId, OrderStatus.INCOMPLETE);
        var order = pancakeStore.addPancakes(existingOrder, additionalPancakes);
        OrderLog.logAddPancake(order, pancake);
    }

    public List<String> viewOrder(UUID orderId) {
        return pancakeStore.findOrderById(orderId).getPancakes().stream()
                .map(Pancake::getName)
                .toList();
    }

    public synchronized void removePancakes(Pancake pancake, UUID orderId, int count) {
        var pancakesToRemove = IntStream.range(0, count).boxed().map(x -> pancake).toList();
        var existingOrder = pancakeStore.findOrderById(orderId);
        var newOrder = pancakeStore.removePancakes(existingOrder, pancakesToRemove);
        var removedItems = existingOrder.getPancakes().size() - newOrder.getPancakes().size();
        OrderLog.logRemovePancakes(newOrder, pancake.description(), removedItems);
    }

    public synchronized Order cancelOrder(UUID orderId) {
        var order = pancakeStore.findOrderByIdAndStatus(orderId, OrderStatus.INCOMPLETE);
        pancakeStore.deleteOrder(order);
        OrderLog.logCancelOrder(order);
        return order;
    }

    public List<UUID> listIncompleteOrders() {
        return pancakeStore.findOrdersByStatus(OrderStatus.INCOMPLETE).stream().map(Order::getId).toList();
    }

    public List<UUID> listCompletedOrders() {
        return pancakeStore.findOrdersByStatus(OrderStatus.COMPLETED).stream().map(Order::getId).toList();
    }

    public List<UUID> listPreparedOrders() {
        return pancakeStore.findOrdersByStatus(OrderStatus.PREPARED).stream().map(Order::getId).toList();
    }

    public synchronized Order deliverOrder(UUID orderId) {
        var order = pancakeStore.findOrderByIdAndStatus(orderId, OrderStatus.PREPARED);
        pancakeStore.deleteOrder(order);
        OrderLog.logDeliverOrder(order);
        return order;
    }

    public synchronized Order completeOrder(UUID orderId) {
        var order = pancakeStore.findOrderByIdAndStatus(orderId, OrderStatus.INCOMPLETE);
        var movedOrder = order instanceof ConcreteOrder && order.getPancakes().isEmpty()
                ? Order.Builder.buildNull("cannot complete orders without pancakes!")
                : pancakeStore.moveOrder(order, OrderStatus.COMPLETED);
        OrderLog.logNextStep(movedOrder);
        return movedOrder;
    }

    public synchronized Order prepareOrder(UUID orderId) {
        var order = pancakeStore.findOrderByIdAndStatus(orderId, OrderStatus.COMPLETED);
        var movedOrder = pancakeStore.moveOrder(order, OrderStatus.PREPARED);
        OrderLog.logNextStep(movedOrder);
        return movedOrder;
    }
}
