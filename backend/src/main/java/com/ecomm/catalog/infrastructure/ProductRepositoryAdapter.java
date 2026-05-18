package com.ecomm.catalog.infrastructure;

import com.ecomm.catalog.domain.Product;
import com.ecomm.catalog.domain.ProductRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class ProductRepositoryAdapter implements ProductRepository {

    private final ProductJpaRepository jpa;

    public ProductRepositoryAdapter(ProductJpaRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public Product save(Product product) {
        ProductEntity entity = new ProductEntity(
            product.getId(), product.getSku(), product.getName(), product.getDescription(),
            product.getPrice(), product.getStockQuantity(), product.isActive(),
            product.getCreatedAt(), product.getUpdatedAt()
        );
        ProductEntity saved = jpa.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<Product> findById(UUID id) {
        return jpa.findById(id).map(this::toDomain);
    }

    @Override
    public Optional<Product> findBySku(String sku) {
        return jpa.findBySku(sku).map(this::toDomain);
    }

    @Override
    public List<Product> findAllActive() {
        return jpa.findAllByActiveTrueOrderByCreatedAtDesc().stream().map(this::toDomain).toList();
    }

    @Override
    public void deleteById(UUID id) {
        jpa.deleteById(id);
    }

    private Product toDomain(ProductEntity e) {
        return new Product(e.getId(), e.getSku(), e.getName(), e.getDescription(),
            e.getPrice(), e.getStockQuantity(), e.isActive(), e.getCreatedAt(), e.getUpdatedAt());
    }
}
