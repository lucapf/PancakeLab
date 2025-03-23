package org.pancakelab.service;

import org.pancakelab.model.Ingredient;
import org.pancakelab.model.Order;
import org.pancakelab.model.Pancake;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Pancake expose all the PancakeLab APIs. Is a Singleton
 */
public enum PancakeService {
    INSTANCE;

    private final PancakeStore pancakeStore = new InMemoryPancakeStore();

    public Optional<Order> createOrder(int building, int room) {
        return pancakeStore.createOrder(building, room);
    }

    public synchronized void addPancakes(UUID orderId, int count, Ingredient... ingredients) {
        var pancake = new Pancake.Builder(ingredients).build();
        pancakeStore.addPancake(orderId, pancake, count)
                .ifPresent(o -> OrderLog.logAddPancake(o, pancake));
    }

    public List<String> viewOrder(UUID orderId) {
        return pancakeStore.viewOrder(orderId);
    }

    public synchronized  void removePancakes(Pancake pancake, UUID orderId, int count) {
        pancakeStore.findOrder(orderId).ifPresent(existingOrder -> {
            var newOrder = pancakeStore.removePancake(existingOrder, pancake, count);
            var removedItems = existingOrder.getPancakes().size() - newOrder.getPancakes().size();
            OrderLog.logRemovePancakes(newOrder, pancake.description(), removedItems);
        });
    }

    public synchronized void cancelOrder(UUID orderId) {
        pancakeStore.cancelOrder(orderId).ifPresent(OrderLog::logCancelOrder);
    }

    public synchronized void completeOrder(UUID orderId) {
        pancakeStore.completeOrder(orderId).ifPresent(OrderLog::logNextStep);
    }

    public List<UUID> listCompletedOrders() {
        return pancakeStore.listCompletedOrders();
    }

    public synchronized  void prepareOrder(UUID orderId) {
        pancakeStore.preparedOrder(orderId).ifPresent(OrderLog::logNextStep);
    }

    public List<UUID> listPreparedOrders() {
        return pancakeStore.listPreparedOrders();
    }

    public synchronized  Optional<Order> deliverOrder(UUID orderId) {
        var orderWithPancakes = pancakeStore.deliverOrder(orderId);
        orderWithPancakes.ifPresent(OrderLog::logDeliverOrder);
        return orderWithPancakes;
    }
}
