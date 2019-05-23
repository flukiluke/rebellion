package com.swen90004.rebellion;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Configuration {
    private static final String CONFIG_FILE = "config.properties";
    private static Properties properties = new Properties();

    public static void loadConfiguration() {
        try {
            InputStream inputStream = new FileInputStream(CONFIG_FILE);
            properties.load(inputStream);
            inputStream.close();
        }
        catch (IOException e) {
            throw new Error(CONFIG_FILE + " not found or read error");
        }
    }

    public static double getDouble(String key) {
        String raw = properties.getProperty(key);
        if (raw == null) {
            throw new Error("Property " + key + " not defined");
        }
        return Double.parseDouble(raw);
    }

    public static int getInt(String key) {
        String raw = properties.getProperty(key);
        if (raw == null) {
            throw new Error("Property " + key + " not defined");
        }
        return Integer.parseInt(raw);    }

    public static boolean getBoolean(String key) {
        String raw = properties.getProperty(key);
        if (raw == null) {
            throw new Error("Property " + key + " not defined");
        }
        return Boolean.parseBoolean(raw);
    }

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
}
