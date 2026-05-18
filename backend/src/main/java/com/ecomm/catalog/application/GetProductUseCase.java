package com.ecomm.catalog.application;

import com.ecomm.catalog.domain.Product;
import com.ecomm.catalog.domain.ProductNotFoundException;
import com.ecomm.catalog.domain.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class GetProductUseCase {

    private final ProductRepository repository;

    public GetProductUseCase(ProductRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public Product execute(UUID id) {
        return repository.findById(id)
            .orElseThrow(() -> new ProductNotFoundException("Product not found: " + id));
    }
}
