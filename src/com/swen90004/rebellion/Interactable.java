package com.swen90004.rebellion;

public interface Interactable {
    void move();
    void tick();
    boolean isPresent();
    Position getPosition();
}
