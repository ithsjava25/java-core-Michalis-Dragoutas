package com.example;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.Objects;

public class ElectronicsProduct extends Product implements Shippable {
    private final int warrantyMonths;
    private final BigDecimal weight; // kg

    public ElectronicsProduct(UUID uuid,
                              String name,
                              Category category,
                              BigDecimal price,
                              int warrantyMonths,
                              BigDecimal weight) {
        super(uuid, name, category, price);

        if (warrantyMonths < 0) {
            throw new IllegalArgumentException("Warranty months cannot be negative.");
        }
        this.warrantyMonths = warrantyMonths;
        this.weight = Objects.requireNonNull(weight, "weight");
    }

    public int warrantyMonths() {
        return warrantyMonths;
    }

    @Override
    public BigDecimal calculateShippingCost() {
        // base 79, add 49 if weight > 5.0 kg
        BigDecimal cost = new BigDecimal("79");
        if (weight.compareTo(new BigDecimal("5.0")) > 0) {
            cost = cost.add(new BigDecimal("49"));
        }
        return cost;
    }

    @Override
    public Double weight() {
        return weight.doubleValue();
    }

    @Override
    public String productDetails() {
        return String.format("Electronics: %s, Warranty: %d months", name(), warrantyMonths);
    }
}