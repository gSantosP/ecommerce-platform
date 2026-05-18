package com.ecomm.catalog.application;

import com.ecomm.catalog.domain.Product;
import com.ecomm.catalog.domain.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class CreateProductUseCase {

    private final ProductRepository repository;

    public CreateProductUseCase(ProductRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Product execute(String sku, String name, String description, BigDecimal price, int stockQuantity) {
        if (repository.findBySku(sku).isPresent()) {
            throw new IllegalArgumentException("SKU already exists: " + sku);
        }
        Product product = Product.create(sku, name, description, price, stockQuantity);
        return repository.save(product);
    }
}
