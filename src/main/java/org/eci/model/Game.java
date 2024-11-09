package org.eci.model;

public class Game {
    private String name;
    private String createdBy;

    // Constructor
    public Game(String name, String createdBy) {
        this.name = name;
        this.createdBy = createdBy;
    }

    // Getters y setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
}
