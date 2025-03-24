package org.pancakelab.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class OrderTest {
    @Test
    public void GivenTwoOrdersWithTheSameId_WhenCheckEquality_ThenTheyAreTeSame_test() {
        var order1 = new Order.Builder(1, 1).build();
        var order2 = new Order.Builder(order1).build();
        var order3 = new Order.Builder(1, 1).build();
        assertEquals(order1, order1);
        assertEquals(order1, order2);
        assertEquals(order1.hashCode(), order2.hashCode());
        assertNotEquals(order1, order3);
        assertNotEquals(order1.hashCode(), order3.hashCode());
        assertNotEquals(order1, Order.Builder.buildNull("invalid"));
    }
}
