package org.pancakelab.service;

import org.pancakelab.InMemoryPancakeStore;
import org.pancakelab.PancakeStore;
import org.pancakelab.model.Order;
import org.pancakelab.model.OrderWithPancakes;
import org.pancakelab.model.PancakesRecipe;

import java.util.*;

public enum PancakeService {
    INSTANCE;
    public PancakeService getInstance (){
        return INSTANCE;
    }
    private final PancakeStore pancakeStore = new InMemoryPancakeStore();

    public Order createOrder(int building, int room) {
        return pancakeStore.createOrder(building, room);
    }

    public void addDarkChocolatePancake(UUID orderId, int count) {
        pancakeStore.addPancake(orderId, PancakesRecipe.DARK_CHOCOLATE, count)
                .ifPresent(o -> OrderLog.logAddPancake(o, PancakesRecipe.DARK_CHOCOLATE));
    }

    public void addDarkChocolateWhippedCreamPancake(UUID orderId, int count) {
        pancakeStore.addPancake(orderId, PancakesRecipe.DARK_CHOCOLATE_WHIPPED_CREAM, count)
                .ifPresent(o -> OrderLog.logAddPancake(o, PancakesRecipe.DARK_CHOCOLATE_WHIPPED_CREAM));
    }

    public void addDarkChocolateWhippedCreamHazelnutsPancake(UUID orderId, int count) {
        pancakeStore.addPancake(orderId, PancakesRecipe.DARK_CHOCOLATE_WHIPPED_CREAM_HAZELNUT, count)
                .ifPresent(o -> OrderLog.logAddPancake(o, PancakesRecipe.DARK_CHOCOLATE_WHIPPED_CREAM_HAZELNUT));
    }

    public void addMilkChocolatePancake(UUID orderId, int count) {
        pancakeStore.addPancake(orderId, PancakesRecipe.MILK_CHOCOLATE, count)
                .ifPresent(o -> OrderLog.logAddPancake(o, PancakesRecipe.MILK_CHOCOLATE));
    }

    public void addMilkChocolateHazelnutsPancake(UUID orderId, int count) {
        pancakeStore.addPancake(orderId, PancakesRecipe.MILK_CHOCOLATE_HAZELNUTS, count)
                .ifPresent(o -> OrderLog.logAddPancake(o, PancakesRecipe.MILK_CHOCOLATE));
    }

    public List<String> viewOrder(UUID orderId) {
        return pancakeStore.viewOrder(orderId);
    }

    public void removePancakes(PancakesRecipe pancakesRecipe, UUID orderId, int count) {
        pancakeStore.findOrder(orderId).ifPresent( existingOrder ->{
            var newOrder = pancakeStore.removePancake(existingOrder, pancakesRecipe, count);
            var removedItems = existingOrder.getPancakes().size() - newOrder.getPancakes().size();
            OrderLog.logRemovePancakes(newOrder, pancakesRecipe.description(), removedItems);
        });
    }

    public void cancelOrder(UUID orderId) {
        pancakeStore.cancelOrder(orderId).ifPresent(OrderLog::logCancelOrder);
    }

    public void completeOrder(UUID orderId) {
        pancakeStore.completeOrder(orderId);
    }

    public Set<UUID> listCompletedOrders() {
        return pancakeStore.listCompletedOrders();
    }

    public void prepareOrder(UUID orderId) {
        pancakeStore.preparedOrder(orderId);
    }

    public Set<UUID> listPreparedOrders() {
        return pancakeStore.listPreparedOrders();
    }

    public Optional<OrderWithPancakes> deliverOrder(UUID orderId) {
        var orderWithPancakes = pancakeStore.deliverOrder(orderId);
        orderWithPancakes.ifPresent(OrderLog::logDeliverOrder);
        return orderWithPancakes;
    }
}
