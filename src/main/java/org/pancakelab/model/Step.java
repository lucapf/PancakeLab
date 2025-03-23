package org.pancakelab.model;

public enum Step {
    NONE("NULL STEP"), INCOMPLETE("created"), COMPLETED("completed"), PREPARED("prepared"), DELIVERED("delivered");
    private final String description;

    Step(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }
}
