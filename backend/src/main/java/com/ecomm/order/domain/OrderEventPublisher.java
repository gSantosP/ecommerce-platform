package com.ecomm.order.domain;

/**
 * Outbound port for publishing order events.
 */
public interface OrderEventPublisher {
    void publishOrderCreated(Order order);
    void publishOrderCancelled(Order order);
}
