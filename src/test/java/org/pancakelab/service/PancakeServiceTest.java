package org.pancakelab.service;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.pancakelab.model.Order;
import org.pancakelab.model.Pancake;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;
import static org.pancakelab.model.Ingredient.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PancakeServiceTest {
    private final static String DARK_CHOCOLATE_PANCAKE_DESCRIPTION = "DARK_CHOCOLATE";
    private final static String MILK_CHOCOLATE_PANCAKE_DESCRIPTION = "MILK_CHOCOLATE";
    private final static String MILK_CHOCOLATE_HAZELNUTS_PANCAKE_DESCRIPTION = "MILK_CHOCOLATE_HAZELNUTS";
    PancakeService pancakeService = PancakeService.INSTANCE;
    Order order;

    @Test
    @org.junit.jupiter.api.Order(10)
    public void GivenOrderDoesNotExist_WhenCreatingOrder_ThenOrderCreatedWithCorrectData_Test() {
        // setup

        // exercise
        order = pancakeService.createOrder(10, 20);
        assertSame(Order.Type.CONCRETE, order.getType());
        assertEquals("building: 10 room: 20", order.getDescription());
        assertEquals(10, order.getBuilding());
        assertEquals(20, order.getRoom());
        // verify
        // tear down
    }

    @Test
    @org.junit.jupiter.api.Order(11)
    public void GivenInvalidAddress_WhenCreatingOrder_ThenOrderIsEmpty_Test() {
        var invalidAddressOrder = pancakeService.createOrder(-1, 0);
        assertSame(Order.Type.INVALID, invalidAddressOrder.getType());
        assertEquals("the address is not valid", invalidAddressOrder.getDescription());
    }

    @Test
    @org.junit.jupiter.api.Order(20)
    public void GivenOrderExists_WhenAddingPancakes_ThenCorrectNumberOfPancakesAdded_Test() {
        // setup

        // exercise
        pancakeService.addPancakes(order.getId(), 3, DARK_CHOCOLATE);
        pancakeService.addPancakes(order.getId(), 3, MILK_CHOCOLATE);
        pancakeService.addPancakes(order.getId(), 3, MILK_CHOCOLATE, HAZELNUTS);

        // verify
        List<String> ordersPancakes = pancakeService.viewOrder(order.getId());
        assertEquals(List.of(DARK_CHOCOLATE_PANCAKE_DESCRIPTION,
                DARK_CHOCOLATE_PANCAKE_DESCRIPTION,
                DARK_CHOCOLATE_PANCAKE_DESCRIPTION,
                MILK_CHOCOLATE_PANCAKE_DESCRIPTION,
                MILK_CHOCOLATE_PANCAKE_DESCRIPTION,
                MILK_CHOCOLATE_PANCAKE_DESCRIPTION,
                MILK_CHOCOLATE_HAZELNUTS_PANCAKE_DESCRIPTION,
                MILK_CHOCOLATE_HAZELNUTS_PANCAKE_DESCRIPTION,
                MILK_CHOCOLATE_HAZELNUTS_PANCAKE_DESCRIPTION), ordersPancakes);

        // tear down
    }

    @Test
    @org.junit.jupiter.api.Order(11)
    public void GivenWrongUUID_WhenViewOrder_ThenResultIsEmpty_Test() {
        List<String> nonExistingOrder = pancakeService.viewOrder(UUID.randomUUID());
        assertTrue(nonExistingOrder.isEmpty());
    }

    @Test
    @org.junit.jupiter.api.Order(12)
    public void GivenNullId_WhenViewOrder_ThenResultIsEmpty_Test() {
        List<String> nullOrder = pancakeService.viewOrder(null);
        assertTrue(nullOrder.isEmpty());
    }


    @Test
    @org.junit.jupiter.api.Order(30)
    public void GivenPancakesExists_WhenRemovingPancakes_ThenCorrectNumberOfPancakesRemoved_Test() {
        // setup
        var darkChocolatePancake = new Pancake.Builder(DARK_CHOCOLATE).build();
        var milkChocolatePancake = new Pancake.Builder(MILK_CHOCOLATE).build();
        var milkChocolateHazelnutsPancake = new Pancake.Builder(MILK_CHOCOLATE, HAZELNUTS).build();

        // exercise
        pancakeService.removePancakes(darkChocolatePancake, order.getId(), 2);
        pancakeService.removePancakes(milkChocolatePancake, order.getId(), 3);
        pancakeService.removePancakes(milkChocolateHazelnutsPancake, order.getId(), 1);

        // verify
        List<String> ordersPancakes = pancakeService.viewOrder(order.getId());

        assertEquals(List.of(DARK_CHOCOLATE_PANCAKE_DESCRIPTION,
                MILK_CHOCOLATE_HAZELNUTS_PANCAKE_DESCRIPTION,
                MILK_CHOCOLATE_HAZELNUTS_PANCAKE_DESCRIPTION), ordersPancakes);

        // tear down
    }

    @Test
    @org.junit.jupiter.api.Order(40)
    public void GivenOrderExists_WhenCompletingOrder_ThenOrderCompleted_Test() {
        // setup

        // exercise
        pancakeService.completeOrder(order.getId());

        // verify
        List<UUID> completedOrdersOrders = pancakeService.listCompletedOrders();
        assertTrue(completedOrdersOrders.contains(order.getId()));

        // tear down
    }

    @Test
    @org.junit.jupiter.api.Order(50)
    public void GivenOrderExists_WhenPreparingOrder_ThenOrderPrepared_Test() {
        // setup

        // exercise
        pancakeService.prepareOrder(order.getId());

        // verify
        List<UUID> completedOrders = pancakeService.listCompletedOrders();
        assertFalse(completedOrders.contains(order.getId()));

        List<UUID> preparedOrders = pancakeService.listPreparedOrders();
        assertTrue(preparedOrders.contains(order.getId()));

        // tear down
    }

    @Test
    @org.junit.jupiter.api.Order(60)
    public void GivenOrderExists_WhenDeliveringOrder_ThenCorrectOrderReturnedAndOrderRemovedFromTheDatabase_Test() {
        // setup
        List<String> pancakesToDeliver = pancakeService.viewOrder(order.getId());

        // exercise
        var deliveredOrder = pancakeService.deliverOrder(order.getId());

        // verify
        List<UUID> completedOrders = pancakeService.listCompletedOrders();
        assertFalse(completedOrders.contains(order.getId()));

        List<UUID> preparedOrders = pancakeService.listPreparedOrders();
        assertFalse(preparedOrders.contains(order.getId()));

        List<String> ordersPancakes = pancakeService.viewOrder(order.getId());
        assertSame(Order.Type.CONCRETE, deliveredOrder.getType());

        assertEquals(List.of(DARK_CHOCOLATE_PANCAKE_DESCRIPTION,
                MILK_CHOCOLATE_HAZELNUTS_PANCAKE_DESCRIPTION,
                MILK_CHOCOLATE_HAZELNUTS_PANCAKE_DESCRIPTION), ordersPancakes);
        assertEquals(order.getId(), deliveredOrder.getId());
        var pancakeNames = deliveredOrder.getPancakes().stream().map(Pancake::getName).toList();
        assertEquals(pancakesToDeliver, pancakeNames);

        // tear down
        order = null;
    }

    @Test
    @org.junit.jupiter.api.Order(70)
    public void GivenOrderExists_WhenCancellingOrder_ThenOrderAndPancakesRemoved_Test() {
        // setup
        var newOrder = pancakeService.createOrder(10, 20);
        assertSame(Order.Type.CONCRETE, newOrder.getType());
        pancakeService.addPancakes(newOrder.getId(), 2, DARK_CHOCOLATE);
        pancakeService.addPancakes(newOrder.getId(), 2, DARK_CHOCOLATE, WHIPPED_CREAM, HAZELNUTS);

        // exercise
        pancakeService.cancelOrder(newOrder.getId());

        // verify
        List<UUID> completedOrders = pancakeService.listCompletedOrders();
        assertFalse(completedOrders.contains(newOrder.getId()));

        List<UUID> preparedOrders = pancakeService.listPreparedOrders();
        assertFalse(preparedOrders.contains(newOrder.getId()));

        List<String> ordersPancakes = pancakeService.viewOrder(newOrder.getId());

        assertEquals(List.of(), ordersPancakes);

        // tear down
    }

    @Test
    @org.junit.jupiter.api.Order(71)
    public void GivenConcurrentChangesToOrder_ThenOrderMustBeConsistent_Test() {
        var cycles = 100;
        var newOrder = pancakeService.createOrder(10, 10);

        try (var executors = Executors.newFixedThreadPool(5)) {
            for (int i = 0; i < cycles; i++) {
                executors.submit(() -> pancakeService.addPancakes(newOrder.getId(), 1, PLAIN));
            }
            executors.shutdown();
        }
        assertEquals(cycles, pancakeService.viewOrder(newOrder.getId()).size());
    }

}
