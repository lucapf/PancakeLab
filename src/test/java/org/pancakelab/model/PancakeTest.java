package org.pancakelab.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PancakeTest {
    @Test
    public void GivenTwoPancakes_IfSameIngredients_ThenIsTheSameRecipe() {
        var pancake1 = new Pancake.Builder(Ingredient.HAZELNUTS).build();
        var pancake2 = new Pancake.Builder(Ingredient.HAZELNUTS).build();
        var pancake3 = new Pancake.Builder(Ingredient.DARK_CHOCOLATE).build();
        assertEquals(pancake1, pancake2);
        assertEquals(pancake1.hashCode(), pancake2.hashCode());
        assertNotEquals(pancake1, pancake3);
        assertNotEquals(pancake1.hashCode(), pancake3.hashCode());
    }
}
