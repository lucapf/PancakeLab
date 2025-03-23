package org.pancakelab.service;

import org.pancakelab.model.Order;
import org.pancakelab.model.Pancake;

class OrderLog {
    private static final StringBuilder log = new StringBuilder();

    public static void logAddPancake(Order order, Pancake pancake) {

        log.append("Added pancake with description '%s' ".formatted(pancake.description()))
                .append("to order %s containing %d pancakes, ".formatted(order.getId(),
                        order.getPancakes().size()))
                .append("for building %d, room %d.".formatted(order.getBuilding(),
                        order.getRoom()));
    }

    public static void logRemovePancakes(Order order, String description, int count) {

        log.append("Removed %d pancake(s) with description '%s' ".formatted(count, description))
                .append("from order %s now containing %d pancakes, ".formatted(order.getId(), order.getPancakes().size()))
                .append("for building %d, room %d.".formatted(order.getBuilding(), order.getRoom()));
    }
    public static void logNextStep(Order o) {
        log.append("%s order %s with %d pancakes ".formatted(o.getStep().getDescription(), o.getId(), o.getPancakes().size()))
                .append("for building %d, room %d.".formatted(o.getBuilding(), o.getPancakes().size()));
    }


    public static void logCancelOrder(Order o) {
        log.append("Cancelled order %s with %d pancakes ".formatted(o.getId(), o.getPancakes().size()))
                .append("for building %d, room %d.".formatted(o.getBuilding(), o.getPancakes().size()));
    }

    public static void logDeliverOrder(Order order) {
        log.append("Order %s with %d pancakes ".formatted(order.getId(), order.getPancakes().size()))
                .append("for building %d, room %d out for delivery.".formatted(order.getBuilding(), order.getRoom()));
    }
}
