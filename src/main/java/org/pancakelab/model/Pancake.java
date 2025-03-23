package org.pancakelab.model;

import java.util.Comparator;
import java.util.HashSet;
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
        private List<Ingredient> ingredients;
        private String name;

        private List<Ingredient> removeDuplicatedAndSortIngredients(){
            return new HashSet<>(ingredients).stream()
                    .sorted(Comparator.comparingInt(Ingredient::index)).toList(); //use Set to remove duplicates
        }

        public Builder(Ingredient... providedIngredients) {
            this.ingredients = List.of(providedIngredients);
        }

        public Pancake build() {
            ingredients = ingredients.isEmpty() ? List.of(Ingredient.PLAIN) : ingredients;
            ingredients = removeDuplicatedAndSortIngredients();
            name  = ingredients.stream().map(Ingredient::name).collect(Collectors.joining("_"));
            return new Pancake(this);
        }
    }
}
