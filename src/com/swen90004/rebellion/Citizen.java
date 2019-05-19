package com.swen90004.rebellion;

public class Citizen implements Interactable {
    private Position position;
    private final Map map;

    // Random values in the range [0,1)
    private final double perceivedHardship;
    private final double riskAversion;

    // How many square the citizen can see
    private final int vision = Configuration.getInt("vision");

    private int turnsInJail = 0;
    private boolean active = false;


    public Citizen(Map map, Position position) {
        this.map = map;
        this.position = position;
        perceivedHardship = Math.random();
        riskAversion = Math.random();
    }

    @Override
    public Position getPosition() {
        return position;
    }

    @Override
    public void move() {
        if (isInJail())
            return;
        Position newPosition = map
                .getNeighbourhood(position, vision)
                .getEmptyPosition();
        if (newPosition != null)
            position = newPosition;
    }

    @Override
    public void tick() {
        if (turnsInJail > 0)
            turnsInJail--;
    }

    public boolean isInJail() {
        return turnsInJail > 0;
    }

    public boolean isRebelling() {
        return active;
    }

    public void determineBehaviour() {
        if (isInJail())
            return;
        active = grievance() - riskAversion * estimatedArrestProbability()
                > Configuration.getDouble("threshold");
    }

    private double grievance() {
        return perceivedHardship * (1 - Configuration.getDouble("governmentLegitimacy"));
    }

    private double estimatedArrestProbability() {
        Map neighbourhood = map.getNeighbourhood(position, vision);
        // Doubles so that the division works properly
        double copCount = neighbourhood.getCops().size();
        double rebelCount = neighbourhood.getActiveCitizens().size();
        return 1 - Math.exp(-Configuration.getDouble("k")
                * Math.floor(copCount / (rebelCount + 1)));
    }

    public void jail(int turns) {
        turnsInJail = turns;
    }
}
