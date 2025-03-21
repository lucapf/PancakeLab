package org.pancakelab.model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PancakesRecipeTest {
    @Test
    public void check_Ingredients() {
        assertEquals(List.of("dark chocolate", "whipped cream", "hazelnuts"),
                PancakesRecipe.DARK_CHOCOLATE_WHIPPED_CREAM_HAZELNUT.ingredients());
        assertEquals(List.of("milk chocolate", "hazelnuts"), PancakesRecipe.MILK_CHOCOLATE_HAZELNUTS.ingredients());
    }

}
