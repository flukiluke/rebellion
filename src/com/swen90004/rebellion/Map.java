package com.swen90004.rebellion;

import java.util.ArrayList;
import java.util.List;

public class Map {
    private ArrayList<Interactable> map;

    public void initialiseBoard() {

    }

    public Map getNeighbourhood(Position position, int vision) {
        return new Map();
    }

    public Position getEmptyPosition() {
        return new Position(0,0);
    }

    public List<Citizen> getActiveCitizens() {
        return new ArrayList<Citizen>();
    }

    public List<Cop> getCops() {
        return new ArrayList<Cop>();
    }
}
