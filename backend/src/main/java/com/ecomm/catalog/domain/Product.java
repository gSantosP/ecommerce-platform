package com.ecomm.catalog.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Product aggregate root.
 * Business invariants enforced in constructor and methods.
 */
public class Product {
    private final UUID id;
    private final String sku;
    private String name;
    private String description;
    private BigDecimal price;
    private int stockQuantity;
    private boolean active;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Product(UUID id, String sku, String name, String description,
                   BigDecimal price, int stockQuantity, boolean active,
                   LocalDateTime createdAt, LocalDateTime updatedAt) {
        if (id == null) throw new IllegalArgumentException("id required");
        if (sku == null || sku.isBlank()) throw new IllegalArgumentException("sku required");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("name required");
        if (price == null || price.signum() < 0) throw new IllegalArgumentException("price must be >= 0");
        if (stockQuantity < 0) throw new IllegalArgumentException("stock must be >= 0");

        this.id = id;
        this.sku = sku;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.active = active;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Product create(String sku, String name, String description, BigDecimal price, int stockQuantity) {
        LocalDateTime now = LocalDateTime.now();
        return new Product(UUID.randomUUID(), sku, name, description, price, stockQuantity, true, now, now);
    }

    /** Decrement stock. Throws if insufficient. */
    public void decrementStock(int quantity) {
        if (quantity <= 0) throw new IllegalArgumentException("quantity must be > 0");
        if (stockQuantity < quantity) {
            throw new InsufficientStockException(
                "Insufficient stock for product " + sku + ": have " + stockQuantity + ", requested " + quantity
            );
        }
        this.stockQuantity -= quantity;
        this.updatedAt = LocalDateTime.now();
    }

    /** Restore stock (e.g. order cancelled). */
    public void restoreStock(int quantity) {
        if (quantity <= 0) throw new IllegalArgumentException("quantity must be > 0");
        this.stockQuantity += quantity;
        this.updatedAt = LocalDateTime.now();
    }

    public void updatePrice(BigDecimal newPrice) {
        if (newPrice == null || newPrice.signum() < 0) throw new IllegalArgumentException("price must be >= 0");
        this.price = newPrice;
        this.updatedAt = LocalDateTime.now();
    }

    public void deactivate() {
        this.active = false;
        this.updatedAt = LocalDateTime.now();
    }

    public UUID getId() { return id; }
    public String getSku() { return sku; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public BigDecimal getPrice() { return price; }
    public int getStockQuantity() { return stockQuantity; }
    public boolean isActive() { return active; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
