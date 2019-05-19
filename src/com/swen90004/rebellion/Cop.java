package com.swen90004.rebellion;

import java.util.List;

public class Cop implements Interactable {
    private Position position;
    private final Map map;

    // How many square the cop can see
    private final int vision = Configuration.getInt("vision");

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
        Position newPosition = map
                .getNeighbourhood(position, vision)
                .getEmptyPosition();
        if (newPosition != null)
            position = newPosition;
    }

    @Override
    public void tick() {

    }

    public void enforce() {
        List<Citizen> citizens = map.getCitizensInVision(position, vision);
        if (citizens.size() == 0) {
            return;
        }

        int maxJailTerm = Configuration.getInt("max-jail-term");
        citizens.get((int)(Math.random() * citizens.size())).jail((int)(Math.random() * (maxJailTerm + 1)));
    }
}
