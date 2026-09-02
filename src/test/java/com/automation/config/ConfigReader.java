package com.automation.config;

import com.automation.config.models.Settings;
import tools.jackson.databind.ObjectMapper;

import java.io.InputStream;

public class ConfigReader {
    private static Settings settings;

    private ConfigReader() {

    }

    public static Settings getSettings() {
        if (settings == null) {
            loadSettings();
        }

        return settings;
    }

    private static void loadSettings() {
        try (InputStream inputStream = ConfigReader.class
                .getClassLoader()
                .getResourceAsStream("appsettings.json")) {

            if (inputStream == null) {
                throw new IllegalStateException("File not found");
            }

            ObjectMapper mapper = new ObjectMapper();
            settings = mapper.readValue(inputStream, Settings.class);

        } catch (Exception e) {
            throw new RuntimeException("Unable to load settings file", e);
        }
    }
}
