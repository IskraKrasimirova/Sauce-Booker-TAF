package com.automation.config.models;

public class Settings {
    public UiSettings ui;
    public ApiSettings api;

    public static class  UiSettings {
        public String baseUrl;
        public String browser;
        public boolean headless;
        public int timeoutSeconds;
        public String username;
        public String password;
    }

    public static class ApiSettings {
        public String baseUrl;
        public String username;
        public String password;
    }
}
