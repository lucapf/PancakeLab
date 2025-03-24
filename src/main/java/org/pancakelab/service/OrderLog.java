package org.pancakelab.service;

import org.pancakelab.model.ConcreteOrder;
import org.pancakelab.model.Order;
import org.pancakelab.model.Pancake;

class OrderLog {

   private synchronized static void writeMessage(Order order , String actionName,String message) {
       message = order instanceof  ConcreteOrder
               ?message:"cannot perform %s - reason: %s".formatted(actionName,order.getDescription());
               System.out.println(message);
   }
    public static void logAddPancake(Order order, Pancake pancake) {
        writeMessage(order,"add Pancacke",
            "Add pancake: %s to order id %s - %s containing %d pancakes ."
       .formatted(pancake.description(),order.getId(), order.getDescription(),order.getPancakes().size() ));
    }

    public static void logRemovePancakes(Order order, int count) {
        writeMessage(order,"remove Pancake",
            "Removed %d pancake(s)  from order %s now containing %d pancakes, %s."
                        .formatted(count, order.getId(), order.getPancakes().size(),order.getDescription()));
    }

    public static void logNextStep(Order order) {
        writeMessage(order,"move next Order Status",
                "order %s with %d pancakes %s moved to %s"
                .formatted(order.getId(), order.getPancakes().size(),order.getDescription(),order.getStatus()));
    }


    public static void logCancelOrder(Order order) {
        writeMessage(order,"Cancel Order",
                "Cancelled order %s with %d pancakes %s."
                .formatted(order.getId(), order.getPancakes().size(),order.getDescription()));
    }

    public static void logDeliverOrder(Order order) {
        writeMessage(order,"Deliver order",
                "Order %s with %d pancakes %s out for delivery."
                        .formatted(order.getId(), order.getPancakes().size(),order.getDescription()));
    }

    public static void logCreateOrder(Order order) {
        writeMessage(order,"Create Order",
                "Order %s %s created."
                .formatted(order.getId(), order.getDescription()));

    }
}
