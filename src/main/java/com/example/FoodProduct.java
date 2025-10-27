package com.example;

import java.math.BigDecimal;
import java.util.UUID;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Food product: perishable and shippable.
 */
public class FoodProduct extends Product implements Perishable, Shippable {
    private final LocalDate expirationDate;
    private BigDecimal weight; // kg

    public FoodProduct(UUID uuid,
                       String name,
                       Category category,
                       BigDecimal price,
                       LocalDate expirationDate,
                       BigDecimal weight) {
        super(uuid, name, category, price);

        if (price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Price cannot be negative.");
        }
        if (weight.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Weight cannot be negative.");
        }
        this.expirationDate = Objects.requireNonNull(expirationDate, "expirationDate");
        this.weight = Objects.requireNonNull(weight, "weight");
    }

    @Override
    public LocalDate expirationDate() {
        return expirationDate;
    }

    @Override
    public BigDecimal calculateShippingCost() {
        // shipping rule: cost = weight * 50
        return weight.multiply(new BigDecimal("50"));
    }

    @Override
    public Double weight() {
        return weight.doubleValue();
    }

    @Override
    public String productDetails() {
        return String.format("Food: %s, Expires: %s", name(), expirationDate.toString());
    }
}