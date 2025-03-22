package org.pancakelab;

import org.pancakelab.model.Order;
import org.pancakelab.model.OrderWithPancakes;
import org.pancakelab.model.PancakesRecipe;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * manage the persistence of the orders.
 * We may need to get rid by the In Memory management soon!
 */
public interface PancakeStore {
    Order createOrder(int building, int room);

    Optional<OrderWithPancakes> findOrder(UUID orderId);

    Optional<OrderWithPancakes> addPancake(UUID orderId, PancakesRecipe pancakesRecipe, int count);

    List<String> viewOrder(UUID uuid);

    OrderWithPancakes removePancake(
            OrderWithPancakes orderWithPancakes, PancakesRecipe pancakesRecipe, int count);

    Optional<OrderWithPancakes> cancelOrder(UUID orderId);

    void completeOrder(UUID orderId);

    Set<UUID> listCompletedOrders();

    void preparedOrder(UUID orderId);

    Set<UUID> listPreparedOrders();

    Optional<OrderWithPancakes> deliverOrder(UUID orderId);
}
