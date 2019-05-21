package com.swen90004.rebellion;

import java.util.List;
import java.util.Random;

public class Simulation {
    public static Random random = new Random();

    public static void main(String[] args) {
        Configuration.loadConfiguration();
	    Map map = new Map(Configuration.getInt("boardSize"));
	    map.initialiseBoard();
	    for (int i = 0; i < 3; i++) {
            // Movement rule
            map.getInteractables().forEach(Interactable::move);

            // Rebellion rule
            map.getCitizens().forEach(Citizen::determineBehaviour);

            // Arrest rule
            map.getCops().forEach(Cop::enforce);

            // Decrease jail times
            map.getCitizens().forEach(Citizen::jailTurn);

            outputValues(map);
        }
    }

    private static void outputValues(Map map) {
        List<Citizen> citizens = map.getCitizens();
        long quiet = citizens.stream().filter(c -> !c.isRebelling() && c.isPresent()).count();
        long active = citizens.stream().filter(Citizen::isRebelling).count();
        long jailed = citizens.stream().filter(Citizen::isInJail).count();
        System.out.format("%d %d %d\n", quiet, active, jailed);
    }


}
