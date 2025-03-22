package org.pancakelab;

import org.pancakelab.model.Order;
import org.pancakelab.model.OrderWithPancakes;
import org.pancakelab.model.PancakesRecipe;

import java.util.*;
import java.util.stream.IntStream;

/**
 * Store the order status in memory.
 * not designed to be extended.
 */
public final class InMemoryPancakeStore implements PancakeStore {
    /**
     * an order to be delivered goes through following steps:
     * incomplete: customer might modify the order
     * completed:  customer completed the order and goes to the chicken
     * prepared: the order is ready to be shipped
     * delivered: customer got the order
     */

    private static final Map<UUID, OrderWithPancakes> incompleteOrders = new HashMap<>();
    private static final Map<UUID, OrderWithPancakes> completedOrders = new HashMap<>();
    private static final Map<UUID, OrderWithPancakes> preparedOrders = new HashMap<>();
    private static final Map<UUID, OrderWithPancakes> deliveredOrders = new HashMap<>();


    @Override
    public Order createOrder(int building, int room) {
        var o = OrderWithPancakes.of(Order.of(building, room));
        incompleteOrders.put(o.getOrderId(), o);
        return o.getOrder();
    }

    @Override
    public Optional<OrderWithPancakes> findOrder(UUID orderId) {
        return Utils.searchForOrder(orderId, incompleteOrders, completedOrders,
                        preparedOrders, deliveredOrders).stream()
                .findAny();
    }

    /**
     * add new pancakes to an existing order.
     * Only incomplete order are modifiable!
     *
     * @param orderId         : the order id
     * @param pancakesRecipe: the Recipe to remove
     * @param count:          items to remove
     */
    @Override
    public Optional<OrderWithPancakes> addPancake(UUID orderId, PancakesRecipe pancakesRecipe, int count) {
        var existingOrder = findOrder(orderId);
        if (existingOrder.isEmpty()) return Optional.empty();
        var additionalPancakes =
                IntStream.range(0, count).boxed().map(x -> pancakesRecipe).toList();
        var updatedOrder = OrderWithPancakes.addPancakes(existingOrder.get(), additionalPancakes);
        incompleteOrders.put(orderId, updatedOrder);
        return existingOrder;
    }

    @Override
    public List<String> viewOrder(UUID uuid) {
        return findOrder(uuid).stream()
                .map(OrderWithPancakes::getPancakes).flatMap(Collection::stream)
                .map(PancakesRecipe::name)
                .toList();
    }

    @Override
    public OrderWithPancakes removePancake(OrderWithPancakes orderWithPancakes,
                                           PancakesRecipe pancakesRecipe, int count) {
        var o = Utils.removeItem(orderWithPancakes, pancakesRecipe, count);
        incompleteOrders.put(o.getOrderId(),o);
        return o;

    }

    @Override
    public Optional<OrderWithPancakes> cancelOrder(UUID orderId) {
        var o = Optional.ofNullable(incompleteOrders.get(orderId));
        incompleteOrders.remove(orderId);
        return o;
    }

    @Override
    public void completeOrder(UUID orderId) {
        Utils.searchForOrder(orderId, incompleteOrders).ifPresent(o -> {
            incompleteOrders.remove(orderId);
            completedOrders.put(orderId, o);
        });

    }

    @Override
    public Set<UUID> listCompletedOrders() {
        return completedOrders.keySet();
    }

    @Override
    public void preparedOrder(UUID orderId) {
        Utils.searchForOrder(orderId, completedOrders).ifPresent(o -> {
            completedOrders.remove(orderId);
            preparedOrders.put(orderId, o);
        });
    }

    @Override
    public Set<UUID> listPreparedOrders() {
        return preparedOrders.keySet();
    }

    @Override
    public Optional<OrderWithPancakes> deliverOrder(UUID orderId) {
        var o = Utils.searchForOrder(orderId, preparedOrders);
        o.ifPresent(oo -> {
            preparedOrders.remove(orderId);
            deliveredOrders.put(orderId,oo);
        });
        return o;
    }
}
