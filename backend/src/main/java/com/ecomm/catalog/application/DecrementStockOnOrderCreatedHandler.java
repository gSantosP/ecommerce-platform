package com.ecomm.catalog.application;

import com.ecomm.catalog.domain.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Reacts to OrderCreated event by decrementing product stock.
 * Demonstrates inter-domain communication via Kafka.
 *
 * Flow: Order domain publishes order.created → this handler in Catalog domain
 *       consumes it and decrements stock atomically.
 */
@Component
public class DecrementStockOnOrderCreatedHandler {

    private static final Logger log = LoggerFactory.getLogger(DecrementStockOnOrderCreatedHandler.class);

    private final ProductRepository productRepository;

    public DecrementStockOnOrderCreatedHandler(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @SuppressWarnings("unchecked")
    @KafkaListener(topics = "order.created", groupId = "catalog-service")
    @Transactional
    public void handle(Map<String, Object> event) {
        log.info("Received order.created event: orderId={}", event.get("orderId"));
        try {
            Object itemsObj = event.get("items");
            if (!(itemsObj instanceof List<?> itemsList)) {
                log.warn("No items in event or invalid format");
                return;
            }

            for (Object itemObj : itemsList) {
                if (!(itemObj instanceof Map<?, ?> itemMap)) continue;
                Map<String, Object> item = (Map<String, Object>) itemMap;

                UUID productId = UUID.fromString(item.get("productId").toString());
                int quantity = ((Number) item.get("quantity")).intValue();

                productRepository.findById(productId).ifPresent(product -> {
                    try {
                        product.decrementStock(quantity);
                        productRepository.save(product);
                        log.info("Decremented stock for product {}: -{} (remaining: {})",
                            product.getSku(), quantity, product.getStockQuantity());
                    } catch (Exception e) {
                        log.error("Failed to decrement stock for product {}: {}",
                            productId, e.getMessage());
                    }
                });
            }
        } catch (Exception e) {
            log.error("Failed to process order.created event", e);
        }
    }
}
