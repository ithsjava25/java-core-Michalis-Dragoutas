package com.example;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Category value object.
 * - private constructor
 * - factory method Category.of(String)
 * - validation of null / blank
 * - normalization (Initial capital)
 * - flyweight caching (same instance for same normalized name)
 */
public final class Category {

    // Flyweight cache keyed by normalized name
    private static final Map<String, Category> CACHE = new ConcurrentHashMap<>();

    private final String name;

    // Private constructor so no public constructors are visible
    private Category(String name) {
        this.name = name;
    }

    /**
     * Factory method to create or return an existing Category.
     *
     * @param name raw name (must not be null or blank)
     * @return Category instance with normalized name (Initial capital)
     */
    public static Category of(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Category name can't be null");
        }
        String trimmed = name.trim();
        if (trimmed.isEmpty()) {
            throw new IllegalArgumentException("Category name can't be blank");
        }

        // Normalize: First letter uppercase, rest lowercase
        String normalized;
        if (trimmed.length() == 1) {
            normalized = trimmed.substring(0, 1).toUpperCase();
        } else {
            normalized = trimmed.substring(0, 1).toUpperCase() + trimmed.substring(1).toLowerCase();
        }

        // Return cached instance (flyweight)
        return CACHE.computeIfAbsent(normalized, Category::new);
    }

    /**
     * Public accessor used by tests.
     */
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Category{" + name + '}';
    }
}