package org.pancakelab.service;

import org.pancakelab.model.Ingredient;
import org.pancakelab.model.Order;
import org.pancakelab.model.OrderStatus;
import org.pancakelab.model.Pancake;

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
    public Order createOrder(int building, int room) {
        return pancakeStore.createOrder(building, room);
    }

    public synchronized void addPancakes(UUID orderId, int count, Ingredient... ingredients) {
        var pancake = new Pancake.Builder(ingredients).build();
        var additionalPancakes =
                IntStream.range(0, count).boxed().map(x -> pancake).toList();

        var order = pancakeStore.addPancakes(orderId,OrderStatus.INCOMPLETE , additionalPancakes);
        OrderLog.logAddPancake(order, pancake);
    }

    public List<String> viewOrder(UUID orderId) {
        return pancakeStore.findOrderById(orderId).getPancakes().stream()
                .map(Pancake::getName)
                .toList();
    }

    public synchronized void removePancakes(Pancake pancake, UUID orderId, int count) {
        var existingOrder = pancakeStore.findOrderById(orderId);
        var pancakesToRemove =
                IntStream.range(0, count).boxed().map(x -> pancake).toList();
        var newOrder = pancakeStore.removePancakes(existingOrder, pancakesToRemove);
        var removedItems = existingOrder.getPancakes().size() - newOrder.getPancakes().size();
        OrderLog.logRemovePancakes(newOrder, pancake.description(), removedItems);
    }

    public synchronized void cancelOrder(UUID orderId) {
        var order = pancakeStore.deleteOrder(orderId);
        OrderLog.logCancelOrder(order);
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
        var order = pancakeStore.findOrderById(orderId);
        var orderWithPancakes = order.getStatus() == OrderStatus.PREPARED
                ?pancakeStore.deleteOrder(orderId)
                :new Order.Builder().setDescription("order id: %s status %s not found").build();
        OrderLog.logDeliverOrder(orderWithPancakes);
        return orderWithPancakes;
    }
    public synchronized Order completeOrder(UUID orderId) {
        var movedOrder = pancakeStore.moveOrder(orderId, OrderStatus.INCOMPLETE, OrderStatus.COMPLETED);
        OrderLog.logNextStep(movedOrder);
        return movedOrder;
    }

    public synchronized Order prepareOrder(UUID orderId) {
        var movedOrder = pancakeStore.moveOrder(orderId, OrderStatus.COMPLETED, OrderStatus.PREPARED);
        OrderLog.logNextStep(movedOrder);
        return movedOrder;
    }
}
