package org.pancakelab.model;

public enum OrderStatus {
    INCOMPLETE("created"), COMPLETED("completed"), PREPARED("prepared");
    private final String description;

    OrderStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }
}
