package com.richardphan.teamrandomizer;

import java.io.Serializable;

public class Player implements Serializable {
    private String name;
    private boolean active;
    private boolean captain;

    public Player(String name) {
        this.name = name;
        this.active = true;
        this.captain = false;
    }

    public Player(String name, boolean active) {
        this.name = name;
        this.active = active;
        this.captain = false;
    }

    public Player(String name, boolean active, boolean captain) {
        this.name = name;
        this.active = active;
        this.captain = captain;
    }

    public void toggleActive() {
        this.active = !this.active;
    }

    public void toggleCaptain() {
        this.captain = !this.captain;
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

    public boolean getCaptain() {
        return captain;
    }
}
