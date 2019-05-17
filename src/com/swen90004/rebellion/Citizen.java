package com.swen90004.rebellion;

public class Citizen implements Interactable {
    private Position position;
    private final Map map;

    private final double perceivedHardship;

    private int turnsInJail = 0;
    private boolean active = true;


    public Citizen(Map map) {
        this.map = map;
        perceivedHardship = 0.5; // TODO: Properly initialise
    }

    @Override
    public Position getPosition() {
        return position;
    }

    @Override
    public void move() {

    }

    @Override
    public void tick() {

    }

    public boolean isInJail() {
        return false;
    }

    public boolean isRebelling() {
        return false;
    }

    public void determineBehaviour() {

    }

    public void jail(int turns) {

    }
}
