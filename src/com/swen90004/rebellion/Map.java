package com.swen90004.rebellion;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Map Class
 *
 * It is responsible for:
 *  - Initialising the 'board'
 *  - Storing all the interactable objects along with their positions
 *  - help interactables 'see' what is in their neighbourhood
 *  - providing interactable objects to Simulation class for updating and output
 *
 * @author Luke Ceddia [834076]
 * @author Nir Palombo [863972]
 * @author Eric Sciberras [761250]
 *
 */

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

    /**
     * Populates the board with Citizens and Cops according to the parameters
     * initialCopDensity and initialCitizenDensity
     */
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

    /**
     * Returns a subset of the Map representing only what the interactable can see.
     *
     * @param position - The position of the interactable calling the method
     * @param vision - The radius the interactable can 'see'
     * @return Map - A map consisting of only what the interactable can see
     */
    public Map getNeighbourhood(Position position, int vision) {
        // Iterates through the Interactables 'Field of vision' and appends
        // Interactables into a new Map
        HashMap<Position, List<Interactable>> subset = new HashMap<>();
        int xWrapped, yWrapped;
        for (int x = position.x - vision; x <= position.x + vision; x++) {
            for (int y = position.y - vision; y <= position.y + vision; y++) {
                xWrapped = (x + size) % size;
                yWrapped = (y + size) % size;
                if (Math.hypot(position.x - x, position.y - y) < vision) {
                    Position p = new Position(xWrapped, yWrapped);
                    List<Interactable> cell = mapData.get(p);
                    if (cell == null)
                        continue;
                    subset.put(p, cell);
                }
            }
        }
        return new Map(subset);
    }

    /**
     * Iterates through Mapdata to find empty positions and randomly returns one.
     *
     * @return position - An empty position.
     */
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

    /**
     * Checks if a given position contains a "present" interactable.
     * By "present" we mean any Interactable that isn't in jail.
     *
     * @param position
     * @return boolean
     */
    public boolean isPositionEmpty(Position position) {
        List<Interactable> interactableList = mapData.get(position);
        if (interactableList.size() == 0 ||
                interactableList.stream().filter(Interactable::isPresent).count() == 0) {
            return true;
        }
        return false;
    }

    /**
     * @return list of interactables within mapData
     */
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

    /**
     * @return list of citizens within mapData
     */
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

    /**
     * @return list of active (rebelling) citizens within mapData
     */
    public List<Citizen> getActiveCitizens() {
        return getCitizens().stream()
                .filter(Citizen::isRebelling)
                .filter(Citizen::isPresent)
                .collect(Collectors.toList());
    }

    /**
     * @return list of cops within mapData
     */
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

    /**
     * Used to update an interactables position on the Map
     *
     * @param interactable - The interactable to move
     * @param position - Current position
     * @param newPosition - Position to move to
     */
    public void moveInteractable(Interactable interactable, Position position, Position newPosition) {
        mapData.get(position).remove(interactable);
        mapData.get(newPosition).add(interactable);
    }
}
