package com.swen90004.rebellion;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Configuration {
    private static final String CONFIG_FILE = "config.properties";
    static Properties properties = new Properties();

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
}
