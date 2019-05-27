package com.swen90004.rebellion;
/**
 * Interactable Interface
 *
 * It is responsible for:
 *  - Enforcing the behaviour of agents (Cops & Citizens)
 *
 * @author Luke Ceddia [834076]
 * @author Nir Palombo [863972]
 * @author Eric Sciberras [761250]
 *
 */


public interface Interactable {
    void move();
    boolean isPresent();
    Position getPosition();
}
