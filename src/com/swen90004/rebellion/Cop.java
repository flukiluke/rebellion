package com.swen90004.rebellion;

public class Cop implements Interactable {
    private Position position;
    private final Map map;

    public Cop(Map map, Position position) {
        this.map = map;
        this.position = position;
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
