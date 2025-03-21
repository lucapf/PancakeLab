package org.pancakelab;

import org.pancakelab.model.OrderWithPancakes;
import org.pancakelab.model.PancakesRecipe;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
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
    public static Optional<OrderWithPancakes> searchForOrder(UUID uuid, HashMap<UUID, OrderWithPancakes>... orders) {
        AtomicReference<OrderWithPancakes> orderWithPancakes = new AtomicReference<>();
        for (var h : orders) {
            Optional.ofNullable(h.get(uuid))
                    .ifPresent(orderWithPancakes::set);
        }
        return Optional.ofNullable(orderWithPancakes.get());
    }

    public static OrderWithPancakes removeItem(OrderWithPancakes o, PancakesRecipe pancakesRecipe, int count) {
        var listPancakes = o.getPancakes();
        for (int i = 0; i < count; i++) {
            listPancakes.removeIf(p -> p == pancakesRecipe);
        }
        return OrderWithPancakes.of(o.getOrder(), listPancakes);
    }
}
