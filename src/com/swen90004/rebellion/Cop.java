package com.swen90004.rebellion;

import java.util.List;

/**
 * Cop Class
 *
 * It is responsible for:
 *  - Jailing rebelling Citizens
 *
 * @author Luke Ceddia [834076]
 * @author Nir Palombo [863972]
 * @author Eric Sciberras [761250]
 *
 */

public class Cop implements Interactable {
    private Position position;
    private final Map map;

    // The radius that the cop can see
    private final int vision = Configuration.getInt("vision");

    public Cop(Map map, Position position) {
        this.map = map;
        this.position = position;
    }

    /**
     * Gets Cop's Position
     *
     * @return Position - the objects Position
     */
    @Override
    public Position getPosition() {
        return position;
    }

    /**
     * Governs the cop's move behaviour
     *
     * Cop's can always move unless their neighbourhood is full
     */
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

    /**
     * Indicates if Cop is on the board. For cops this is always true.
     *
     * @return boolean - Indicates if Cop is on board
     */
    @Override
    public boolean isPresent() {
        return true;
    }

    /**
     * Governs the enforce mechanic, where a Cop can jail a rebelling citizen
     *
     * Looks for rebelling citizens in it's neighbourhood and randomly selects one to jail
     * for a time between 0 and maxJailTerm.
     */
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
