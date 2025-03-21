package org.pancakelab.service;

import org.pancakelab.model.OrderWithPancakes;
import org.pancakelab.model.PancakesRecipe;

public class OrderLog {
    private static final StringBuilder log = new StringBuilder();

    public static void logAddPancake(OrderWithPancakes orderWithPancakes, PancakesRecipe pancakesRecipe) {

        log.append("Added pancake with description '%s' ".formatted(pancakesRecipe.description()))
                .append("to order %s containing %d pancakes, ".formatted(orderWithPancakes.getOrder().getId(),
                        orderWithPancakes.getPancakesNames().size()))
                .append("for building %d, room %d.".formatted(orderWithPancakes.getOrder().getBuilding(),
                        orderWithPancakes.getOrder().getRoom()));
    }

    public static void logRemovePancakes(OrderWithPancakes order, String description, int count) {

        log.append("Removed %d pancake(s) with description '%s' ".formatted(count, description))
                .append("from order %s now containing %d pancakes, ".formatted(order.getOrder().getId(), order.getPancakesNames().size()))
                .append("for building %d, room %d.".formatted(order.getOrder().getBuilding(), order.getOrder().getRoom()));
    }

    public static void logCancelOrder(OrderWithPancakes o) {
        log.append("Cancelled order %s with %d pancakes ".formatted(o.getOrder().getId(), o.getPancakes().size()))
                .append("for building %d, room %d.".formatted(o.getOrder().getBuilding(), o.getPancakes().size()));
    }

    public static void logDeliverOrder(OrderWithPancakes order) {
        log.append("Order %s with %d pancakes ".formatted(order.getOrder().getId(), order.getPancakes().size()))
                .append("for building %d, room %d out for delivery.".formatted(order.getOrder().getBuilding(), order.getOrder().getRoom()));
    }
}
