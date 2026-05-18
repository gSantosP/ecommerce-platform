package com.ecomm.order.infrastructure;

import com.ecomm.order.domain.Order;
import com.ecomm.order.domain.OrderItem;
import com.ecomm.order.domain.OrderRepository;
import com.ecomm.order.domain.OrderStatus;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class OrderRepositoryAdapter implements OrderRepository {

    private final OrderJpaRepository jpa;

    public OrderRepositoryAdapter(OrderJpaRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public Order save(Order order) {
        OrderEntity entity = new OrderEntity(
            order.getId(), order.getCustomerId(), order.getStatus().name(),
            order.getTotalAmount(), order.getCreatedAt(), order.getUpdatedAt()
        );
        List<OrderItemEntity> items = order.getItems().stream()
            .map(i -> new OrderItemEntity(i.id(), i.productId(), i.productName(), i.quantity(), i.unitPrice()))
            .toList();
        entity.setItems(items);
        return toDomain(jpa.save(entity));
    }

    @Override
    public Optional<Order> findById(UUID id) {
        return jpa.findById(id).map(this::toDomain);
    }

    @Override
    public List<Order> findByCustomerId(UUID customerId) {
        return jpa.findByCustomerIdOrderByCreatedAtDesc(customerId).stream().map(this::toDomain).toList();
    }

    private Order toDomain(OrderEntity e) {
        List<OrderItem> items = e.getItems().stream()
            .map(i -> new OrderItem(i.getId(), i.getProductId(), i.getProductName(), i.getQuantity(), i.getUnitPrice()))
            .toList();
        return new Order(
            e.getId(), e.getCustomerId(), OrderStatus.valueOf(e.getStatus()),
            items, e.getTotalAmount(), e.getCreatedAt(), e.getUpdatedAt()
        );
    }
}
