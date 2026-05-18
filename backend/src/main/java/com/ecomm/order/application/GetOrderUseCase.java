package com.ecomm.order.application;

import com.ecomm.order.domain.Order;
import com.ecomm.order.domain.OrderNotFoundException;
import com.ecomm.order.domain.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class GetOrderUseCase {

    private final OrderRepository repository;

    public GetOrderUseCase(OrderRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public Order execute(UUID id) {
        return repository.findById(id)
            .orElseThrow(() -> new OrderNotFoundException("Order not found: " + id));
    }
}
