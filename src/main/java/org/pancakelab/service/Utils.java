package org.pancakelab.service;

import org.pancakelab.model.Ingredient;
import org.pancakelab.model.Order;
import org.pancakelab.model.Pancake;

import java.util.*;
import java.util.stream.Stream;

public class Utils {
    public static List<Pancake> concat(List<Pancake> list1, List<Pancake> list2) {
        return Stream.concat(list1.stream(), list2.stream()).toList();
    }

    public static Order removeItem(Order o, Pancake pancake, int count) {
        Objects.requireNonNull(o);
        List<Pancake> listPancakes = new ArrayList<>(o.getPancakes());
        for (int i = 0; i < count; i++) {
            listPancakes.remove(pancake);
        }
        var newOrder = new Order.Builder(o).setListPancakes(listPancakes).build();
        assert newOrder.isPresent();
        return newOrder.get();
    }

    public static List<Ingredient> removeDuplicatedAndSort(List<Ingredient> ingredients) {
        Objects.requireNonNull(ingredients);
        return new HashSet<>(ingredients).stream()
                .sorted(Comparator.comparingInt(Ingredient::index)).toList(); //use Set to remove duplicates
    }

}
