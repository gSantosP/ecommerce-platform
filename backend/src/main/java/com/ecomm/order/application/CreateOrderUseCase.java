package com.ecomm.order.application;

import com.ecomm.catalog.domain.InsufficientStockException;
import com.ecomm.catalog.domain.Product;
import com.ecomm.catalog.domain.ProductNotFoundException;
import com.ecomm.catalog.domain.ProductRepository;
import com.ecomm.order.domain.Order;
import com.ecomm.order.domain.OrderEventPublisher;
import com.ecomm.order.domain.OrderItem;
import com.ecomm.order.domain.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class CreateOrderUseCase {

    private static final Logger log = LoggerFactory.getLogger(CreateOrderUseCase.class);

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OrderEventPublisher eventPublisher;

    public CreateOrderUseCase(OrderRepository orderRepository, ProductRepository productRepository, OrderEventPublisher eventPublisher) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public Order execute(UUID customerId, List<CreateOrderItem> requestedItems) {
        if (requestedItems == null || requestedItems.isEmpty()) {
            throw new IllegalArgumentException("Order must have at least one item");
        }

        List<OrderItem> items = new ArrayList<>();

        for (CreateOrderItem req : requestedItems) {
            Product product = productRepository.findById(req.productId())
                .orElseThrow(() -> new ProductNotFoundException("Product not found: " + req.productId()));

            if (!product.isActive()) {
                throw new IllegalArgumentException("Product inactive: " + product.getSku());
            }
            if (product.getStockQuantity() < req.quantity()) {
                throw new InsufficientStockException(
                    "Insufficient stock for " + product.getSku() + ": have " + product.getStockQuantity() + ", requested " + req.quantity()
                );
            }

            items.add(new OrderItem(
                UUID.randomUUID(),
                product.getId(),
                product.getName(),
                req.quantity(),
                product.getPrice()
            ));
        }

        Order order = Order.create(customerId, items);
        Order saved = orderRepository.save(order);

        // Publish event - Catalog will consume and decrement stock
        eventPublisher.publishOrderCreated(saved);
        log.info("Order created: {} (total={})", saved.getId(), saved.getTotalAmount());

        return saved;
    }

    public record CreateOrderItem(UUID productId, int quantity) {}
}
