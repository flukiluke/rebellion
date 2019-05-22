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
        if (newPosition != null) {
            map.moveInteractable(this, position, newPosition);
            position = newPosition;
        }
    }

    @Override
    public boolean isPresent() {
        return true;
    }

    public void enforce() {
        List<Citizen> citizens = map.getNeighbourhood(position, vision).getActiveCitizens();
        if (citizens.size() == 0) {
            return;
        }

        int maxJailTerm = Configuration.getInt("maxJailTerm");
        Citizen citizen = citizens.get(Simulation.random.nextInt(citizens.size()));
        int jailTerm = Simulation.random.nextInt(maxJailTerm);
        citizen.jail(jailTerm);
        Position newPosition = citizen.getPosition();
        map.moveInteractable(this, position, newPosition);
        position = newPosition;
    }
}
