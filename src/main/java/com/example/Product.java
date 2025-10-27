package com.example;

import java.math.BigDecimal;
import java.util.UUID;

public abstract class Product {

    private final UUID uuid;
    private final String name;
    private final Category category;
    private BigDecimal price;

    protected Product(UUID uuid, String name, Category category, BigDecimal price) {
        this.uuid = uuid;
        this.name = name;
        this.category = category;
        this.price = price;
    }

    public UUID uuid() {
        return uuid;
    }

    public String name() {
        return name;
    }

    public Category category() {
        return category;
    }

    public BigDecimal price() {
        return price;
    }

    public void price(BigDecimal newPrice) {
        this.price = newPrice;
    }

    public abstract String productDetails();
}