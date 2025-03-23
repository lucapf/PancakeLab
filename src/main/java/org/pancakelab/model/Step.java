package org.pancakelab.model;

public enum Step {
    INCOMPLETE("created"), COMPLETED("completed"), PREPARED("prepared"), DELIVERED("delivered");
    private final String description;
    Step(String description){
        this.description = description;
    }
    public String getDescription(){
        return this.description;
    }
}
