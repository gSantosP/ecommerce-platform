package com.ecomm.catalog.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Outbound port for Product persistence.
 */
public interface ProductRepository {
    Product save(Product product);
    Optional<Product> findById(UUID id);
    Optional<Product> findBySku(String sku);
    List<Product> findAllActive();
    void deleteById(UUID id);
}
