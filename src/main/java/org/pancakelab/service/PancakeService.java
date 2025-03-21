package org.pancakelab.service;

import org.pancakelab.InMemoryPancakeStore;
import org.pancakelab.PancakeStore;
import org.pancakelab.model.Order;
import org.pancakelab.model.OrderWithPancakes;
import org.pancakelab.model.PancakesRecipe;

import java.util.*;

public class PancakeService {
    PancakeStore pancakeStore = new InMemoryPancakeStore();

    public Order createOrder(int building, int room) {
        return pancakeStore.createOrder(building, room);
    }

    public void addDarkChocolatePancake(UUID orderId, int count) {
        pancakeStore.addPancakeToExistingOrder(orderId, PancakesRecipe.DARK_CHOCOLATE, count)
                .ifPresent(o -> OrderLog.logAddPancake(o, PancakesRecipe.DARK_CHOCOLATE));
    }

    public void addDarkChocolateWhippedCreamPancake(UUID orderId, int count) {
        pancakeStore.addPancakeToExistingOrder(orderId, PancakesRecipe.DARK_CHOCOLATE_WHIPPED_CREAM, count)
                .ifPresent(o -> OrderLog.logAddPancake(o, PancakesRecipe.DARK_CHOCOLATE_WHIPPED_CREAM));
    }

    public void addDarkChocolateWhippedCreamHazelnutsPancake(UUID orderId, int count) {
        pancakeStore.addPancakeToExistingOrder(orderId, PancakesRecipe.DARK_CHOCOLATE_WHIPPED_CREAM_HAZELNUT, count)
                .ifPresent(o -> OrderLog.logAddPancake(o, PancakesRecipe.DARK_CHOCOLATE_WHIPPED_CREAM_HAZELNUT));
    }

    public void addMilkChocolatePancake(UUID orderId, int count) {
        pancakeStore.addPancakeToExistingOrder(orderId, PancakesRecipe.MILK_CHOCOLATE, count)
                .ifPresent(o -> OrderLog.logAddPancake(o, PancakesRecipe.MILK_CHOCOLATE));
    }

    public void addMilkChocolateHazelnutsPancake(UUID orderId, int count) {
        pancakeStore.addPancakeToExistingOrder(orderId, PancakesRecipe.MILK_CHOCOLATE_HAZELNUTS, count)
                .ifPresent(o -> OrderLog.logAddPancake(o, PancakesRecipe.MILK_CHOCOLATE));
    }

    public List<String> viewOrder(UUID orderId) {
        return pancakeStore.viewOrder(orderId);
    }

    public void removePancakes(String description, UUID orderId, int count) {
        var existingOrder = pancakeStore.findOrder(orderId);
        var recipe = Arrays.stream(PancakesRecipe.values())
                .filter(v -> v.description().equals(description)).findFirst();
        if (existingOrder.isPresent() && recipe.isPresent()) {
            var newOrder = pancakeStore.removePancakeToExistingOrder(existingOrder.get(), recipe.get(), count);
            var removedItems = existingOrder.get().getPancakes().size() - newOrder.getPancakes().size();
            OrderLog.logRemovePancakes(newOrder, description, removedItems);
        }

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
