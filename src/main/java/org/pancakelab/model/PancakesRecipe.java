package org.pancakelab.model;

import java.util.Collections;
import java.util.List;

import static org.pancakelab.Utils.concatIngredients;

public enum PancakesRecipe {
    PLAIN(Collections.emptyList()), //type String is inferred
    DARK_CHOCOLATE(concatIngredients(PLAIN, "dark chocolate")),
    DARK_CHOCOLATE_WHIPPED_CREAM(concatIngredients(DARK_CHOCOLATE, "whipped cream")),
    DARK_CHOCOLATE_WHIPPED_CREAM_HAZELNUT(concatIngredients(DARK_CHOCOLATE_WHIPPED_CREAM, "hazelnuts")),
    MILK_CHOCOLATE(concatIngredients(PLAIN, "milk chocolate")),
    MILK_CHOCOLATE_HAZELNUTS(concatIngredients(MILK_CHOCOLATE, "hazelnuts"));

    private final List<String> ingredients;

    PancakesRecipe(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    // no need to enforce immutability List.of is immutable
    public List<String> ingredients() {
        return ingredients;
    }

    public String description() {
        return "Delicious pancake with %s!".formatted(String.join(", ", ingredients()));
    }
}
