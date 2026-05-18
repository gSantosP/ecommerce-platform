package com.ecomm.order.infrastructure;

import com.ecomm.order.domain.Order;
import com.ecomm.order.domain.OrderEventPublisher;
import com.ecomm.shared.config.KafkaTopicsConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Publishes order domain events to Kafka.
 */
@Component
public class KafkaOrderEventPublisher implements OrderEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(KafkaOrderEventPublisher.class);

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public KafkaOrderEventPublisher(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void publishOrderCreated(Order order) {
        Map<String, Object> event = buildEvent(order);
        kafkaTemplate.send(KafkaTopicsConfig.ORDER_CREATED, order.getId().toString(), event);
        log.info("Published order.created: {}", order.getId());
    }

    @Override
    public void publishOrderCancelled(Order order) {
        Map<String, Object> event = buildEvent(order);
        kafkaTemplate.send(KafkaTopicsConfig.ORDER_CANCELLED, order.getId().toString(), event);
        log.info("Published order.cancelled: {}", order.getId());
    }

    private Map<String, Object> buildEvent(Order order) {
        Map<String, Object> event = new HashMap<>();
        event.put("orderId", order.getId().toString());
        event.put("customerId", order.getCustomerId().toString());
        event.put("status", order.getStatus().name());
        event.put("totalAmount", order.getTotalAmount());

        List<Map<String, Object>> items = order.getItems().stream().map(i -> {
            Map<String, Object> m = new HashMap<>();
            m.put("productId", i.productId().toString());
            m.put("productName", i.productName());
            m.put("quantity", i.quantity());
            m.put("unitPrice", i.unitPrice());
            return m;
        }).toList();
        event.put("items", items);

        return event;
    }
}
