package com.swen90004.rebellion;

import java.util.List;
import java.util.Random;

public class Simulation {
    public static Random random = new Random();

    public static void main(String[] args) {
        Configuration.loadConfiguration();
        Configuration.parseCmdLineArgs(args);

	    Map map = new Map(Configuration.getInt("boardSize"));
	    map.initialiseBoard();
	    for (int i = 0; i < Configuration.getInt("iterations"); i++) {
            // Movement rule
            map.getInteractables().forEach(Interactable::move);

            // Rebellion rule
            map.getCitizens().forEach(Citizen::determineBehaviour);

            // Arrest rule
            map.getCops().forEach(Cop::enforce);

            List<Citizen> citizens = map.getCitizens();
            // Decrease jail times
            citizens.forEach(Citizen::jailTurn);

            // Generate stats
            long quiet = citizens.stream().filter(c -> !c.isRebelling() && c.isPresent()).count();
            long active = citizens.stream().filter(Citizen::isRebelling).count();
            long jailed = citizens.stream().filter(Citizen::isInJail).count();
            int visibility;
            if (!Configuration.getBoolean("censorship")) {
                visibility = Configuration.getInt("vision");
            }
            else {
                visibility = Math.max(0, (int) (Configuration.getInt("vision") -
                        Math.exp((active) / 20)));
            }

            // Update visibilities if needed
            if (Configuration.getBoolean("censorship")) {
                map.getCitizens().forEach(c -> c.updateVision(visibility));
            }
            System.out.format("%d %d %d %d\n", quiet, active, jailed, visibility);
	    }
    }
}
