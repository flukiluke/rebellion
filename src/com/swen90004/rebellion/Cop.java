package com.swen90004.rebellion;

public class Cop implements Interactable {
    private Position position;
    private final Map map;

    public Cop(Map map) {
        this.map = map;
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

    public void enforce() {

    }
}
