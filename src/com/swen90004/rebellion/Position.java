package com.swen90004.rebellion;

import java.util.Objects;

/**
 * Position Class
 *
 * It is responsible for:
 *  - Holding X and Y coordinates of interactable objects
 *
 * @author Luke Ceddia [834076]
 * @author Nir Palombo [863972]
 * @author Eric Sciberras [761250]
 *
 */

public class Position {
    public final int x;
    public final int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Compares object to this Position for equality
     *
     * @param o - an object (can be either a Position object or Null)
     * @return boolean - the indicate if object is equal to this Position
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return x == position.x &&
                y == position.y;
    }

    /**
     * Since position is the key of the hashmap and we are overriding
     * equals(), we should also override the default hashCode() function
     * to ensure correct behaviour.
     *
     * @return int - a hash
     */
    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
