package com.ecomm.order.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Order aggregate root.
 */
public class Order {
    private final UUID id;
    private final UUID customerId;
    private OrderStatus status;
    private final List<OrderItem> items;
    private BigDecimal totalAmount;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Order(UUID id, UUID customerId, OrderStatus status, List<OrderItem> items,
                 BigDecimal totalAmount, LocalDateTime createdAt, LocalDateTime updatedAt) {
        if (id == null) throw new IllegalArgumentException("id required");
        if (customerId == null) throw new IllegalArgumentException("customerId required");
        if (items == null || items.isEmpty()) throw new IllegalArgumentException("order must have items");

        this.id = id;
        this.customerId = customerId;
        this.status = status;
        this.items = new ArrayList<>(items);
        this.totalAmount = totalAmount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Order create(UUID customerId, List<OrderItem> items) {
        LocalDateTime now = LocalDateTime.now();
        BigDecimal total = items.stream()
            .map(OrderItem::subtotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        return new Order(UUID.randomUUID(), customerId, OrderStatus.PENDING, items, total, now, now);
    }

    public void confirm() {
        if (status != OrderStatus.PENDING) {
            throw new IllegalStateException("Can only confirm pending orders");
        }
        this.status = OrderStatus.CONFIRMED;
        this.updatedAt = LocalDateTime.now();
    }

    public void cancel() {
        if (status == OrderStatus.CANCELLED) {
            throw new IllegalStateException("Order already cancelled");
        }
        this.status = OrderStatus.CANCELLED;
        this.updatedAt = LocalDateTime.now();
    }

    public UUID getId() { return id; }
    public UUID getCustomerId() { return customerId; }
    public OrderStatus getStatus() { return status; }
    public List<OrderItem> getItems() { return Collections.unmodifiableList(items); }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
