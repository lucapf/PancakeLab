package org.pancakelab.service;

import org.pancakelab.model.Order;
import org.pancakelab.model.Pancake;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * manage the persistence of the orders.
 * We may need to get rid by the In Memory management soon!
 */
interface PancakeStore {
    Optional<Order> createOrder(int building, int room);

    Optional<Order> findOrder(UUID orderId);

    Optional<Order> addPancake(UUID orderId, Pancake pancakesRecipe, int count);

    List<String> viewOrder(UUID uuid);

    Order removePancake(
            Order order, Pancake pancakesRecipe, int count);

    Optional<Order> cancelOrder(UUID orderId);

    Optional<Order> completeOrder(UUID orderId);

    List<UUID> listCompletedOrders();

    Optional<Order> preparedOrder(UUID orderId);

    List<UUID> listPreparedOrders();

    Optional<Order> deliverOrder(UUID orderId);
}
