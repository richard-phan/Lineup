package com.richardphan.teamrandomizer;

public class Player {
    private String name;
    private boolean active;

    public Player(String name) {
        this.name = name;
        this.active = true;
    }

    public Player(String name, boolean active) {
        this.name = name;
        this.active = active;
    }

    public void toggleActive() {
        this.active = !this.active;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean getActive() {
        return active;
    }
}