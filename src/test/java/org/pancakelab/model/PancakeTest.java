package org.pancakelab.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class PancakeTest {
    @Test
    public void GivenTwoPancakes_IfSameIngredients_ThenIsTheSameRecipe(){
        var pancake1 = new Pancake.Builder(Ingredient.HAZELNUTS).build();
        var pancake2 = new Pancake.Builder(Ingredient.HAZELNUTS).build();
        var pancake3 = new Pancake.Builder(Ingredient.DARK_CHOCOLATE).build();
       assertEquals(pancake1, pancake2);
       assertNotEquals(pancake1, pancake3);
    }
}
