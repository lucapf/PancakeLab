package org.pancakelab.service;

import org.junit.jupiter.api.*;
import org.pancakelab.model.*;
import org.pancakelab.model.Order;

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
    private final PancakeService pancakeService = PancakeService.INSTANCE;
    private Order order;

    @Test
    @org.junit.jupiter.api.Order(10)
    public void GivenOrderDoesNotExist_WhenCreatingOrder_ThenOrderCreatedWithCorrectData_Test() {
        // setup

        // exercise
        order = pancakeService.createOrder(10, 20);
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
        assertInstanceOf(NullOrder.class, invalidAddressOrder);
        assertEquals("the address building: -1 room: 0 is not valid", invalidAddressOrder.getDescription());
    }

    @Test
    @org.junit.jupiter.api.Order(12)
    public void GivenANonExistingId_WhenAddingPancakes_nothingHappen_Test() {
        var orderId = UUID.randomUUID();
        var addedOrder = pancakeService.addPancakes(orderId, 1, HAZELNUTS);
        assertInstanceOf(NullOrder.class,addedOrder );
        assertTrue(pancakeService.viewOrder(orderId).isEmpty());
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
    @org.junit.jupiter.api.Order(41)
    public void GivenANewOrder_WhenMovingBetweenStates_OnlyCorrectMovesTakeEffect_Test() {
        // setup
        var newOrder = pancakeService.createOrder(10, 10);
        var orderId = newOrder.getId();
        pancakeService.addPancakes(orderId, 1, MILK_CHOCOLATE);

        // perform
        assertTrue(pancakeService.listIncompleteOrders().contains(orderId));
        assertFalse(pancakeService.listCompletedOrders().contains(orderId));

        var prepareOrder = pancakeService.prepareOrder(orderId);
        assertInstanceOf(NullOrder.class, prepareOrder);

        var deliveredOrder = pancakeService.deliverOrder(orderId);
        assertInstanceOf(NullOrder.class, deliveredOrder);

        var completedOrders = pancakeService.completeOrder(orderId);
        assertEquals(orderId, completedOrders.getId());
        // tear Down
        pancakeService.prepareOrder(orderId);
        pancakeService.deliverOrder(orderId);
    }

    @Test
    @org.junit.jupiter.api.Order(42)
    public void GivenAnOrderStatusCompletedOrPrepared_WhenTryToChange_ItTakesNoEffect_Test() {
        // setup
        var newOrder = pancakeService.createOrder(10, 10);
        var orderId = newOrder.getId();
        pancakeService.addPancakes(orderId, 1, DARK_CHOCOLATE, HAZELNUTS);
        pancakeService.addPancakes(orderId, 1, MILK_CHOCOLATE);
        var configuredPancakes = List.of("DARK_CHOCOLATE_HAZELNUTS", "MILK_CHOCOLATE");

        // perform
        assertEquals(2, pancakeService.viewOrder(orderId).size());
        var completedOrder = pancakeService.completeOrder(orderId);
        assertInstanceOf(ConcreteOrder.class, completedOrder);

        pancakeService.addPancakes(orderId, 1, MILK_CHOCOLATE);
        assertEquals(configuredPancakes, pancakeService.viewOrder(orderId));

        pancakeService.prepareOrder(orderId);
        var cannotBeAdded = pancakeService.addPancakes(orderId, 1, MILK_CHOCOLATE);
        assertInstanceOf(NullOrder.class, cannotBeAdded);
        assertEquals(configuredPancakes, pancakeService.viewOrder(orderId));
        //tear down
        pancakeService.deliverOrder(orderId);
    }


    @Test
    @org.junit.jupiter.api.Order(43)
    public void GivenACompletedOrPreparedOrder_WhenTryToCancel_ThenOrderWontChange_Test() {
        //setup
        var newOrder = pancakeService.createOrder(10, 10);
        var orderId = newOrder.getId();
        pancakeService.addPancakes(orderId, 2, MILK_CHOCOLATE);
        Order completedOrder = pancakeService.completeOrder(orderId);
        assertEquals(orderId, completedOrder.getId());

        // exercise
        assertEquals(OrderStatus.COMPLETED, completedOrder.getStatus());

        var cancelledOrder = pancakeService.cancelOrder(orderId);
        assertInstanceOf(NullOrder.class, cancelledOrder);
        assertEquals("order id: %s with status: INCOMPLETE not found".formatted(orderId), cancelledOrder.getDescription());


        var cancelNonExistingOrder = pancakeService.cancelOrder(UUID.randomUUID());
        assertInstanceOf(NullOrder.class, cancelNonExistingOrder);

        // tear down
        pancakeService.prepareOrder(orderId);
        pancakeService.deliverOrder(orderId);


    }

    @Test
    @org.junit.jupiter.api.Order(44)
    public void GivenAnIncompleteOrderWithoutPancakes_WhenTryToComplete_TheOrderWontChange_Test() {
        var newOrder = pancakeService.createOrder(10, 10);
        var orderId = newOrder.getId();
        var order = pancakeService.completeOrder(orderId);
        assertInstanceOf(NullOrder.class, order);
        assertEquals("order id: %s cannot complete orders without pancakes!".formatted(orderId),
                order.getDescription());
        var nonExistingOrder = pancakeService.completeOrder(UUID.randomUUID());
        assertInstanceOf(NullOrder.class, nonExistingOrder);

        var nullOrder = pancakeService.completeOrder(null);
        assertInstanceOf(NullOrder.class, nullOrder);

        //tear down
        pancakeService.addPancakes(orderId,1 ,DARK_CHOCOLATE );
        pancakeService.completeOrder(orderId);
        pancakeService.prepareOrder(orderId);
        pancakeService.deliverOrder(orderId);
    }

    @Test
    @org.junit.jupiter.api.Order(50)
    public void GivenOrderExists_WhenPreparingOrder_ThenOrderPrepared_Test() {
        // setup

        // exercise
        pancakeService.prepareOrder(order.getId());
        var nonExistingUUID = UUID.randomUUID();

        // verify
        List<UUID> completedOrders = pancakeService.listCompletedOrders();
        assertFalse(completedOrders.contains(order.getId()));

        // completion for a non-existing order take no effect
        pancakeService.completeOrder(nonExistingUUID);
        assertTrue(pancakeService.viewOrder(nonExistingUUID).isEmpty());

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
        assertEquals(List.of(), ordersPancakes);
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
        assertFalse(newOrder instanceof NullOrder);
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
        var cycles = 1000;
        var newOrder = pancakeService.createOrder(10, 10);
        var pancake = new Pancake.Builder(DARK_CHOCOLATE).build();
        try (var executors = Executors.newFixedThreadPool(5)) {
            for (int i = 0; i < cycles; i++) {
                executors.submit(() -> {
                    pancakeService.addPancakes(newOrder.getId(), 1, DARK_CHOCOLATE);
                    var incompleteOrder = pancakeService.createOrder(10, 10);
                    pancakeService.addPancakes(incompleteOrder.getId(), 1, WHIPPED_CREAM);
                    pancakeService.completeOrder(incompleteOrder.getId());
                    pancakeService.createOrder(1, 1);
                });
            }
            executors.shutdown();
        }
        assertEquals(cycles, pancakeService.viewOrder(newOrder.getId()).size());
        assertEquals(cycles + 1, pancakeService.listIncompleteOrders().size());
        assertEquals(cycles, pancakeService.listCompletedOrders().size());
        try (var executors = Executors.newFixedThreadPool(5)) {
            for (int i = 0; i < cycles; i++) {
                executors.submit(() -> pancakeService.removePancakes(pancake, newOrder.getId(), 1));
            }
            executors.shutdown();
        }
        assertEquals(0, pancakeService.viewOrder(newOrder.getId()).size());
        var orders = pancakeService.listIncompleteOrders();
        try (var executors = Executors.newFixedThreadPool(4)) {
            for (var order : orders) executors.submit(() -> pancakeService.cancelOrder(order));
            executors.shutdown();
        }
        assertEquals(0, pancakeService.listIncompleteOrders().size());
    }
}
