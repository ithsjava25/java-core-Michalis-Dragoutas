package com.example;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.math.BigDecimal;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

/**
 * Warehouse singleton-per-name.
 */
public class Warehouse {
    private static final Map<String, Warehouse> INSTANCES = new ConcurrentHashMap<>();
    private static final String DEFAULT_NAME = "default";

    private final String name;
    private final Map<UUID, Product> products = new LinkedHashMap<>(); // preserves insertion order
    private final Set<UUID> changedProducts = new LinkedHashSet<>();

    private Warehouse(String name) {
        this.name = name;
    }

    // No public constructors
    public static Warehouse getInstance() {
        return getInstance(DEFAULT_NAME);
    }

    public static Warehouse getInstance(String name) {
        String key = (name == null) ? DEFAULT_NAME : name;
        return INSTANCES.computeIfAbsent(key, Warehouse::new);
    }

    public String getName() {
        return name;
    }

    public synchronized void addProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null.");
        }
        UUID id = product.uuid();
        if (products.containsKey(id)) {
            throw new IllegalArgumentException("Product with that id already exists, use updateProduct for updates.");
        }
        products.put(id, product);
    }

    public synchronized List<Product> getProducts() {
        // return an unmodifiable copy preserving insertion order
        return Collections.unmodifiableList(new ArrayList<>(products.values()));
    }

    public synchronized Optional<Product> getProductById(UUID id) {
        return Optional.ofNullable(products.get(id));
    }

    public synchronized void updateProductPrice(UUID id, BigDecimal newPrice) {
        Product p = products.get(id);
        if (p == null) {
            throw new NoSuchElementException("Product not found with id: " + id);
        }
        p.price(newPrice);
        changedProducts.add(id);
    }

    public synchronized List<Product> getChangedProducts() {
        return changedProducts.stream()
                .map(products::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public synchronized List<Perishable> expiredProducts() {
        return products.values().stream()
                .filter(p -> p instanceof Perishable)
                .map(p -> (Perishable) p)
                .filter(Perishable::isExpired)
                .collect(Collectors.toList());
    }

    public synchronized List<Shippable> shippableProducts() {
        return products.values().stream()
                .filter(p -> p instanceof Shippable)
                .map(p -> (Shippable) p)
                .collect(Collectors.toList());
    }

    public synchronized void remove(UUID id) {
        products.remove(id);
        changedProducts.remove(id);
    }

    public synchronized boolean isEmpty() {
        return products.isEmpty();
    }

    public synchronized void clearProducts() {
        products.clear();
        changedProducts.clear();
    }

    public synchronized Map<Category, List<Product>> getProductsGroupedByCategories() {
        if (products.isEmpty()) return Collections.emptyMap();
        return products.values().stream()
                .collect(Collectors.groupingBy(Product::category));
    }
}