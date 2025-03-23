package org.pancakelab.service;

import org.pancakelab.model.Order;
import org.pancakelab.model.Pancake;
import org.pancakelab.model.Step;

import java.util.List;
import java.util.UUID;

/**
 * manage the persistence of the orders.
 * We may need to get rid by the In Memory management soon!
 */
interface PancakeStore {
    Order createOrder(int building, int room);

    Order findOrder(UUID orderId);

    Order addPancake(UUID orderId, Pancake pancakesRecipe, int count);

    Order removePancake(Order order, Pancake pancakesRecipe, int count);

    Order cancelOrder(UUID orderId);

    List<UUID> listCompletedOrders();

    List<UUID> listPreparedOrders();

    Order moveOrder(UUID orderId, Step currentStep, Step nextStep);
}
