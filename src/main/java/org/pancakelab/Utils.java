package org.pancakelab;

import org.pancakelab.model.OrderWithPancakes;
import org.pancakelab.model.PancakesRecipe;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Utils {
    public static List<String> concatIngredients(PancakesRecipe p,
                                                 String... additionalIngredients) {
        return Stream.concat(p.ingredients().stream(),
                Stream.of(additionalIngredients)).collect(Collectors.toList());
    }

    public static List<PancakesRecipe> concat(List<PancakesRecipe> l1, List<PancakesRecipe> l2) {
        return Stream.concat(l1.stream(), l2.stream()).toList();
    }

    @SafeVarargs
    public static Optional<OrderWithPancakes> searchForOrder(UUID uuid, Map<UUID, OrderWithPancakes>... orders) {
        AtomicReference<OrderWithPancakes> orderWithPancakes = new AtomicReference<>();
        for (var h : orders) {
            Optional.ofNullable(h.get(uuid))
                    .ifPresent(orderWithPancakes::set);
        }
        return Optional.ofNullable(orderWithPancakes.get());
    }

    public static OrderWithPancakes removeItem(OrderWithPancakes o, PancakesRecipe pancakesRecipe, int count) {
        List<PancakesRecipe> listPancakes = new ArrayList<>(o.getPancakes());
        for (int i = 0; i <count; i++) {
            listPancakes.remove(pancakesRecipe);
        }
        return OrderWithPancakes.of(o.getOrder(), listPancakes);
    }
}
