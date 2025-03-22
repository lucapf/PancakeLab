package org.pancakelab.service;

import org.pancakelab.InMemoryPancakeStore;
import org.pancakelab.PancakeStore;
import org.pancakelab.model.Ingredient;
import org.pancakelab.model.Order;
import org.pancakelab.model.Pancake;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public enum PancakeService {
    INSTANCE;

    private final PancakeStore pancakeStore = new InMemoryPancakeStore();

    public PancakeService getInstance() {
        return INSTANCE;
    }

    public Optional<Order> createOrder(int building, int room) {
        return pancakeStore.createOrder(building, room);
    }

    public void addPancakes(UUID orderId, int count, Ingredient... ingredients) {
        var pancake = new Pancake.Builder(ingredients).build();
        pancakeStore.addPancake(orderId, pancake, count)
                .ifPresent(o -> OrderLog.logAddPancake(o, pancake));
    }

    public List<String> viewOrder(UUID orderId) {
        return pancakeStore.viewOrder(orderId);
    }

    public void removePancakes(Pancake pancake, UUID orderId, int count) {
        pancakeStore.findOrder(orderId).ifPresent(existingOrder -> {
            var newOrder = pancakeStore.removePancake(existingOrder, pancake, count);
            var removedItems = existingOrder.getPancakes().size() - newOrder.getPancakes().size();
            OrderLog.logRemovePancakes(newOrder, pancake.description(), removedItems);
        });
    }

    public void cancelOrder(UUID orderId) {
        pancakeStore.cancelOrder(orderId).ifPresent(OrderLog::logCancelOrder);
    }

    public void completeOrder(UUID orderId) {
        pancakeStore.completeOrder(orderId);
    }

    public List<UUID> listCompletedOrders() {
        return pancakeStore.listCompletedOrders();
    }

    public void prepareOrder(UUID orderId) {
        pancakeStore.preparedOrder(orderId);
    }

    public List<UUID> listPreparedOrders() {
        return pancakeStore.listPreparedOrders();
    }

    public Optional<Order> deliverOrder(UUID orderId) {
        var orderWithPancakes = pancakeStore.deliverOrder(orderId);
        orderWithPancakes.ifPresent(OrderLog::logDeliverOrder);
        return orderWithPancakes;
    }
}
