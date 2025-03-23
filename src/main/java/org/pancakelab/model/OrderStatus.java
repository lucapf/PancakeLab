package org.pancakelab.model;

public enum OrderStatus {
    NONE("NULL STEP"), INCOMPLETE("created"), COMPLETED("completed"), PREPARED("prepared"), DELIVERED("delivered");
    private final String description;

    OrderStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }
}
