package com.swen90004.rebellion;

public class Position {
    public final int x;
    public final int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public double distanceTo(Position other) {
        return Math.hypot(other.x - x, other.y - y);
    }
}
