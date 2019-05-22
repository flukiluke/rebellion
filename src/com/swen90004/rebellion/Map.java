package com.swen90004.rebellion;

import java.util.*;
import java.util.stream.Collectors;

public class Map {
    private HashMap<Position, List<Interactable>> mapData = new HashMap<>();
    private int size;

    public Map(int size) {
        this.size = size;
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                mapData.put(new Position(x, y), new ArrayList<>());
            }
        }
    }

    public Map(HashMap<Position, List<Interactable>> mapData) {
        this.mapData = mapData;
    }

    public void initialiseBoard() {
        int initialCopDensity = Configuration.getInt("initialCopDensity");
        int initialCitizenDensity = Configuration.getInt("initialCitizenDensity");

        // If the sum of the initialCopDensity and initialCitizenDensity is greater
        // then 100% then we cannot place all the mapData on the map
        if( initialCopDensity + initialCitizenDensity > 100 ){
            throw new java.lang.Error("Error: initialCopDensity + initialCitizenDensity must be less than 100");
        }

        // convert densities to number of citizens and cops
        int numOfCops = Math.round(initialCopDensity/100f * size * size) ;
        int numOfCitizens = Math.round(initialCitizenDensity/100f * size * size);

        Position position;
        while (numOfCops-- > 0) {
            position = getEmptyPosition();
            Cop cop = new Cop(this, position);
            mapData.get(position).add(cop);
        }
        while (numOfCitizens-- > 0) {
            position = getEmptyPosition();
            Citizen citizen = new Citizen(this, position);
            mapData.get(position).add(citizen);
        }
    }

    public Map getNeighbourhood(Position position, int vision) {
        HashMap<Position, List<Interactable>> subset = new HashMap<>();
        for (int x = position.x - vision; x <= position.x + vision; x++) {
            for (int y = position.y - vision; y <= position.y + vision; y++) {
                if (Math.hypot(position.x - x, position.y - y) <= vision) {
                    Position p = new Position(x, y);
                    List<Interactable> cell = mapData.get(p);
                    if (cell == null)
                        continue;
                    subset.put(p, cell);
                }
            }
        }
        return new Map(subset);
    }

    public Position getEmptyPosition() {
        List<Position> candidates = new ArrayList<>();
        for (Position p : mapData.keySet()) {
            if (isPositionEmpty(p)) {
                candidates.add(p);
            }
        }
        if (candidates.size() == 0) {
            return null;
        }
        return candidates.get(Simulation.random.nextInt(candidates.size()));
    }

    public boolean isPositionEmpty(Position position) {
        List<Interactable> interactableList = mapData.get(position);
        if (interactableList.size() == 0 ||
                interactableList.stream().filter(Interactable::isPresent).count() == 0) {
            return true;
        }
        return false;
    }

    public List<Interactable> getInteractables() {
        List<Interactable> interactableList = new ArrayList<>();
        for (Position position : mapData.keySet()) {
            if(!isPositionEmpty(position)) {
                for (Interactable interactable : mapData.get(position)) {
                    interactableList.add(interactable);
                }
            }
        }
        return interactableList;
    }

    public List<Citizen> getCitizens() {
        List<Citizen> citizens = new ArrayList<>();
        for (Position position : mapData.keySet()) {
            if(mapData.get(position).size() > 0) {
                for (Interactable interactable : mapData.get(position)) {
                    if(interactable instanceof Citizen){
                        citizens.add((Citizen) interactable);
                    }
                }
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
        for (Position position : mapData.keySet()) {
            if(!isPositionEmpty(position)) {
                for (Interactable interactable : mapData.get(position)) {
                    if(interactable instanceof Cop){
                        cops.add((Cop) interactable);
                    }
                }
            }
        }
        return cops;
    }

    public void moveInteractable(Interactable interactable, Position position, Position newPosition) {
        mapData.get(position).remove(interactable);
        mapData.get(newPosition).add(interactable);
    }
}
