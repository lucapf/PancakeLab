package org.pancakelab.model;

public enum Ingredient {
    PLAIN("no toppings", 0),
    DARK_CHOCOLATE("dark chocolate", 1),
    MILK_CHOCOLATE("milk chocolate", 2),
    WHIPPED_CREAM("whipped cream", 3),
    HAZELNUTS("hazelnuts", 4);
    private final String ingredient;
    private final Integer priority;

    Ingredient(String ingredient, Integer order) {
        this.ingredient = ingredient;
        this.priority = order;
    }

    public int index() {
        return priority;
    }

    public String ingredient() {
        return ingredient;
    }
}
