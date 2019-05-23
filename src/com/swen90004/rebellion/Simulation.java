package com.swen90004.rebellion;

import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Simulation {
    public static Random random = new Random();

    /** Read command line and override any configuration values.
     *
     * @param args The args array passed to main()
     */
    public static void parseCmdLineArgs(String[] args) {
        for (String arg: args) {
            if (arg.equals("--help") || arg.equals("-h")) {
                System.out.println("Here are the default values of the program:");
                Set<String> keys = Configuration.properties.stringPropertyNames();
                for (String key : keys) {
                    System.out.println("  " + key + " = " + Configuration.properties.getProperty(key));
                }
                System.out.println("It can be executed with different values for example:");
                System.out.println("java Simulation --k=2.5 --vision=5");
                System.exit(0);
            }
        }

        // Expect a series of --x=y arguments
        Pattern pattern = Pattern.compile("^--(.+)=(.*)$");
        for (String arg : args) {
            Matcher match = pattern.matcher(arg);
            if (!match.matches()) {
                System.out.println("Argument " + arg + " is not understandable, ignoring");
                continue;
            }
            if (Configuration.properties.getProperty(match.group(1)) != null) {
                Configuration.properties.setProperty(match.group(1), match.group(2));
            } else {
                System.out.println("Wrong argument " + arg + ", ignoring");
            }
        }
    }

    public static void main(String[] args) {
        Configuration.loadConfiguration();
        parseCmdLineArgs(args);

	    Map map = new Map(Configuration.getInt("boardSize"));
	    map.initialiseBoard();
	    for (int i = 0; i < Configuration.getInt("iterations"); i++) {
            outputValues(map);

            // Movement rule
            map.getInteractables().forEach(Interactable::move);

            // Rebellion rule
            map.getCitizens().forEach(Citizen::determineBehaviour);

            // Arrest rule
            map.getCops().forEach(Cop::enforce);

            // Decrease jail times
            map.getCitizens().forEach(Citizen::jailTurn);
        }
	    outputValues(map);
    }

    private static void outputValues(Map map) {
        List<Citizen> citizens = map.getCitizens();
        long quiet = citizens.stream().filter(c -> !c.isRebelling() && c.isPresent()).count();
        long active = citizens.stream().filter(Citizen::isRebelling).count();
        long jailed = citizens.stream().filter(Citizen::isInJail).count();
        System.out.format("%d %d %d\n", quiet, active, jailed);
    }


}
