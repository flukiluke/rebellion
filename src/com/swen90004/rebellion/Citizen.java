package com.swen90004.rebellion;

/**
 * Citizen Class
 *
 * It is responsible for:
 *  - Handling the rebelling and Jailing behaviour of Citizens
 *
 * @author Luke Ceddia [834076]
 * @author Nir Palombo [863972]
 * @author Eric Sciberras [761250]
 *
 */

public class Citizen implements Interactable {
    private Position position;
    private final Map map;

    // Random values in the range [0,1)
    // See report for explanation of these values
    private final double perceivedHardship;
    private final double riskAversion;

    // How many squares radius the citizen can see
    private int vision = Configuration.getInt("vision");

    // If > 0 we are in jail and is a count of how many turn we have left
    private int turnsInJail = 0;

    // Are we rebelling?
    private boolean active = false;

    /**
     * Create a citizen at a given position
     * @param map Map to place citizen on
     * @param position Position to place citizen at
     */
    public Citizen(Map map, Position position) {
        this.map = map;
        this.position = position;
        perceivedHardship = Math.random();
        riskAversion = Math.random();
    }

    /**
     * Gets citizen's Position
     *
     * @return Position - the object's Position
     */
    @Override
    public Position getPosition() {
        return position;
    }

    /**
     * Governs the citizen's move behaviour
     *
     * citizens can move if
     *  - They are not in jail; AND
     *  - The movement parameter is set to TRUE; AND
     *  - Their neighbourhood is not full
     *
     */
    @Override
    public void move() {
        if (isInJail() || !Configuration.getBoolean("movement")) {
            return;
        }
        Position newPosition = map
                .getNeighbourhood(position, vision)
                .getEmptyPosition();
        if (newPosition != null) {
            map.moveInteractable(this, position, newPosition);
            position = newPosition;
        }
    }

    /**
     * Controls Jail behaviour
     *
     * After every tick a jailed citizen's turns in jail will decrease by one.
     */
    public void jailTurn() {
        if (turnsInJail > 0)
            turnsInJail--;
    }

    /**
     * Indicates if citizen is on the board.
     * Citizen's are considered present if they are not in jail.
     *
     * @return boolean
     */
    @Override
    public boolean isPresent() {
        return !isInJail();
    }

    /**
     * @return if the citizen is in jail
     */
    public boolean isInJail() {
        return turnsInJail > 0;
    }

    /**
     * @return if the citizen is rebelling
     */
    public boolean isRebelling() {
        return active;
    }

    /**
     * Governs whether the citizen will rebel or not.
     */
    public void determineBehaviour() {
        if (isInJail())
            return;
        active = grievance() - riskAversion * estimatedArrestProbability()
                > Configuration.getDouble("threshold");
    }

    /**
     * Calculates a citizen's grievance, which is a factor in determining if the citizen
     * will rebel. perceivedHardship is assigned randomly at run-time and governmentLegitimacy
     * is a parameter passed into the simulation.
     * @return double
     */
    private double grievance() {
        return perceivedHardship * (1 - Configuration.getDouble("governmentLegitimacy"));
    }

    /**
     * Calculates a citizen's estimatedArrestProbability which is a factor in determining
     * if the citizen will rebel. This value is dependant on the amount of cops and
     * active (rebelling) citizens in the neighbourhood.
     * @return double
     */
    private double estimatedArrestProbability() {
        Map neighbourhood = map.getNeighbourhood(position, vision);
        // Doubles so that the division works properly
        double copCount = neighbourhood.getCops().size();
        double rebelCount = neighbourhood.getActiveCitizens().size();
        return 1 - Math.exp(-Configuration.getDouble("k")
                * Math.floor(copCount / (rebelCount + 1)));
    }

    /**
     * Govern's the Jailing of a citizen
     * @param turns
     */
    public void jail(int turns) {
        turnsInJail = turns;
        active = false;
    }

    /**
     * Setter for vision
     * Used for the dynamic vision extension of the model.
     * @param vision
     */
    public void updateVision(int vision) {
        this.vision = vision;
    }
}
