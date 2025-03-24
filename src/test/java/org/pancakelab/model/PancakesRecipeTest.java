package org.pancakelab.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PancakesRecipeTest {
    @Test
    public void check_Ingredients() {
        var darkChocolateHazelnuts = new Pancake
                .Builder(Ingredient.HAZELNUTS, Ingredient.DARK_CHOCOLATE,
                Ingredient.WHIPPED_CREAM, Ingredient.DARK_CHOCOLATE).build();
        assertEquals("DARK_CHOCOLATE_WHIPPED_CREAM_HAZELNUTS", darkChocolateHazelnuts.getName());

        var darkChocolate = new Pancake.Builder(Ingredient.DARK_CHOCOLATE).build();
        assertEquals("DARK_CHOCOLATE", darkChocolate.getName());

        var duplicatedIngredients = new Pancake
                .Builder(Ingredient.HAZELNUTS, Ingredient.HAZELNUTS).build();
        assertEquals("HAZELNUTS", duplicatedIngredients.getName());

        var plainPancake = new Pancake.Builder().build();
        assertEquals("", plainPancake.getName());

    }
}
