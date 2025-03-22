package org.pancakelab;

import org.pancakelab.model.Order;
import org.pancakelab.model.Pancake;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * manage the persistence of the orders.
 * We may need to get rid by the In Memory management soon!
 */
public interface PancakeStore {
    Optional<Order> createOrder(int building, int room);

    Optional<Order> findOrder(UUID orderId);

    Optional<Order> addPancake(UUID orderId, Pancake pancakesRecipe, int count);

    List<String> viewOrder(UUID uuid);

    Order removePancake(
            Order order, Pancake pancakesRecipe, int count);

    Optional<Order> cancelOrder(UUID orderId);

    void completeOrder(UUID orderId);

    List<UUID> listCompletedOrders();

    void preparedOrder(UUID orderId);

    List<UUID> listPreparedOrders();

    Optional<Order> deliverOrder(UUID orderId);
}
