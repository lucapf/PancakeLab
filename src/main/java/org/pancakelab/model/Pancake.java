package org.pancakelab.model;

import org.pancakelab.Utils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Pancake {
    private final List<Ingredient> ingredients;
    private final String name;

    private Pancake(Builder builder) {
        this.ingredients = builder.ingredients;
        this.name = builder.name;
    }

    public List<Ingredient> ingredients() {
        return ingredients;
    }

    public String getName() {
        return name;
    }

    public List<String> getIngredientNames() {
        return ingredients.stream().map(Ingredient::name).toList();
    }


    public String description() {
        return "Delicious pancake with %s!".formatted(String.join(", ", getIngredientNames()));
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Pancake pancake)) return false;
        return Objects.equals(name, pancake.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }

    public static class Builder {
        private final List<Ingredient> ingredients;
        private final String name;

        public Builder(Ingredient... providedIngredients) {
            var sortedIngredients = Utils.removeDuplicatedAndSort(List.of(providedIngredients));
            this.ingredients = sortedIngredients.isEmpty() ? List.of(Ingredient.PLAIN) : sortedIngredients;
            this.name = this.ingredients.stream().map(Ingredient::name).collect(Collectors.joining("_"));
        }

        public Pancake build() {
            return new Pancake(this);
        }
    }
}
