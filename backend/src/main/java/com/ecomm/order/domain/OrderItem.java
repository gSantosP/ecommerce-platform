package com.ecomm.order.domain;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderItem(
    UUID id,
    UUID productId,
    String productName,
    int quantity,
    BigDecimal unitPrice
) {
    public OrderItem {
        if (productId == null) throw new IllegalArgumentException("productId required");
        if (productName == null || productName.isBlank()) throw new IllegalArgumentException("productName required");
        if (quantity <= 0) throw new IllegalArgumentException("quantity must be > 0");
        if (unitPrice == null || unitPrice.signum() < 0) throw new IllegalArgumentException("unitPrice must be >= 0");
    }

    public BigDecimal subtotal() {
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }
}
