package com.ecomm.catalog.application;

import com.ecomm.catalog.domain.Product;
import com.ecomm.catalog.domain.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ListProductsUseCase {

    private final ProductRepository repository;

    public ListProductsUseCase(ProductRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<Product> execute() {
        return repository.findAllActive();
    }
}
