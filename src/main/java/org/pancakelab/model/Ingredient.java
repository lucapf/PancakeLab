package org.pancakelab.model;

public enum Ingredient {
    DARK_CHOCOLATE(1),
    MILK_CHOCOLATE(2),
    WHIPPED_CREAM(3),
    HAZELNUTS(4);
    private final Integer priority;

    Ingredient(Integer order) {
        this.priority = order;
    }

    public int index() {
        return priority;
    }

}
