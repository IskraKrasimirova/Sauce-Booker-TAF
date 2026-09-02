package com.automation.ui.models;

public class Product {
    private final String name;
    private final String price;
    private final String description;

    public Product(String name, String price, String description) {
        this.name = name;
        this.price = price;
        this.description = description;
    }

    public String getName() {
        return name;
    }
    public String getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }
}
