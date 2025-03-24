package org.pancakelab.service;

import org.pancakelab.model.Order;
import org.pancakelab.model.OrderStatus;
import org.pancakelab.model.Pancake;

import java.util.List;
import java.util.UUID;

/**
 * manage the persistence of the orders.
 * We may need to get rid by the In Memory management soon!
 */
interface PancakeStore {
    Order createOrder(int building, int room);

    Order findOrderById(UUID orderId);

    Order findOrderByIdAndStatus(UUID orderId, OrderStatus orderStatus);

    Order addPancakes(Order order, List<Pancake> pancakesRecipe);

    Order removePancakes(Order order, List<Pancake> pancakesRecipe);

    void deleteOrder(Order order);

    List<Order> findOrdersByStatus(OrderStatus orderStatus);

    Order moveOrder(Order existingOrder, OrderStatus nextOrderStatus);
}
