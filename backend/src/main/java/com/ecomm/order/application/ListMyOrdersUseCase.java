package com.ecomm.order.application;

import com.ecomm.order.domain.Order;
import com.ecomm.order.domain.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class ListMyOrdersUseCase {

    private final OrderRepository repository;

    public ListMyOrdersUseCase(OrderRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<Order> execute(UUID customerId) {
        return repository.findByCustomerId(customerId);
    }
}
