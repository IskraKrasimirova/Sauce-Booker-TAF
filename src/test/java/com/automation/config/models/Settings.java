package com.automation.config.models;

public class Settings {
    public UiSettings ui;

    public static class  UiSettings {
        public String baseUrl;
        public String browser;
        public boolean headless;
        public int timeoutSeconds;
        public String username;
        public String password;
    }
}
