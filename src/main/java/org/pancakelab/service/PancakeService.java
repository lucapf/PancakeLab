package org.pancakelab.service;

import org.pancakelab.model.Ingredient;
import org.pancakelab.model.Order;
import org.pancakelab.model.Pancake;
import org.pancakelab.model.Step;

import java.util.List;
import java.util.UUID;

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
    public Order createOrder(int building, int room) {
        return pancakeStore.createOrder(building, room);
    }

    public synchronized void addPancakes(UUID orderId, int count, Ingredient... ingredients) {
        var pancake = new Pancake.Builder(ingredients).build();
        var order = pancakeStore.addPancake(orderId, pancake, count);
        OrderLog.logAddPancake(order, pancake);
    }

    public List<String> viewOrder(UUID orderId) {
        return pancakeStore.findOrder(orderId).getPancakes().stream()
                .map(Pancake::getName)
                .toList();
    }

    public synchronized void removePancakes(Pancake pancake, UUID orderId, int count) {
        var existingOrder = pancakeStore.findOrder(orderId);
        var newOrder = pancakeStore.removePancake(existingOrder, pancake, count);
        var removedItems = existingOrder.getPancakes().size() - newOrder.getPancakes().size();
        OrderLog.logRemovePancakes(newOrder, pancake.description(), removedItems);
    }

    public synchronized void cancelOrder(UUID orderId) {
        var order = pancakeStore.cancelOrder(orderId);
        OrderLog.logCancelOrder(order);
    }

    public synchronized void completeOrder(UUID orderId) {
        var movedOrder = pancakeStore.moveOrder(orderId, Step.INCOMPLETE, Step.COMPLETED);
        OrderLog.logNextStep(movedOrder);
    }

    public synchronized void prepareOrder(UUID orderId) {
        var movedOrder = pancakeStore.moveOrder(orderId, Step.COMPLETED, Step.PREPARED);
        OrderLog.logNextStep(movedOrder);
    }

    public List<UUID> listCompletedOrders() {
        return pancakeStore.listCompletedOrders();
    }


    public List<UUID> listPreparedOrders() {
        return pancakeStore.listPreparedOrders();
    }

    public synchronized Order deliverOrder(UUID orderId) {
        var orderWithPancakes = pancakeStore.moveOrder(orderId, Step.PREPARED, Step.DELIVERED);
        OrderLog.logDeliverOrder(orderWithPancakes);
        return orderWithPancakes;
    }
}
