package com.swen90004.rebellion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Map {
    private List<Interactable> interactables = new ArrayList<>();
    private List<Position> positions = new ArrayList<>();
    private int size;

    public Map(int size) {
        this.size = size;
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                positions.add(new Position(x, y));
            }
        }
    }

    public Map(List<Interactable> interactables, List<Position> positions) {
        this.interactables = interactables;
        this.positions = positions;
    }

    public void initialiseBoard() {
        int initialCopDensity = Configuration.getInt("initialCopDensity");
        int initialCitizenDensity = Configuration.getInt("initialCitizenDensity");

        // If the sum of the initialCopDensity and initialCitizenDensity is greater
        // then 100% then we cannot place all the interactables on the map
        if( initialCopDensity + initialCitizenDensity > 100 ){
            throw new java.lang.Error("Error: initialCopDensity + initialCitizenDensity must be less than 100");
        }

        // convert densities to number of citizens and cops
        int numOfCops = Math.round(initialCopDensity/100f * size * size) ;
        int numOfCitizens = Math.round(initialCitizenDensity/100f * size * size);

        Position position;
        while (numOfCops-- > 0) {
            position = getEmptyPosition();
            interactables.add(new Cop(this, position));
        }
        while (numOfCitizens-- > 0) {
            position = getEmptyPosition();
            interactables.add(new Citizen(this, position));
        }
    }

    public Map getNeighbourhood(Position position, int vision) {
        List<Position> positions = this.positions.stream()
                .filter(p -> position.distanceTo(p) <= vision)
                .collect(Collectors.toList());
        List<Interactable> interactables = this.interactables.stream()
                .filter(i -> position.distanceTo(i.getPosition()) <= vision)
                .collect(Collectors.toList());
        return new Map(interactables, positions);
    }

    public Position getEmptyPosition() {
        List<Position> candidates = new ArrayList<>();
        for (Position position : positions) {
            if (!isPositionOccupied(position)) {
                candidates.add(position);
            }
        }
        if (candidates.size() == 0) {
            return null;
        }
        Collections.shuffle(candidates);
        return candidates.get(0);
    }

    public boolean isPositionOccupied(Position position) {
        for (Interactable interactable : interactables) {
            if (position.equals(interactable.getPosition()) && interactable.isPresent()) {
                return false;
            }
        }
        return true;
    }

    public List<Interactable> getInteractables() {
        return interactables;
    }

    public List<Citizen> getCitizens() {
        List<Citizen> citizens = new ArrayList<>();
        for(Interactable interactable : this.interactables) {
            if(interactable instanceof Citizen){
                citizens.add((Citizen) interactable);
            }
        }
        return citizens;
    }

    public List<Citizen> getActiveCitizens() {
        return getCitizens().stream()
                .filter(Citizen::isRebelling)
                .collect(Collectors.toList());
    }

    public List<Cop> getCops() {
        List<Cop> cops = new ArrayList<>();
        for(Interactable interactable : this.interactables) {
            if(interactable instanceof Cop){
                cops.add((Cop) interactable);
            }
        }
        return cops;
    }

}
