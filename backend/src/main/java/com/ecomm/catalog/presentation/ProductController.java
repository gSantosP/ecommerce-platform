package com.ecomm.catalog.presentation;

import com.ecomm.catalog.application.CreateProductUseCase;
import com.ecomm.catalog.application.GetProductUseCase;
import com.ecomm.catalog.application.ListProductsUseCase;
import com.ecomm.catalog.domain.Product;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ListProductsUseCase listUseCase;
    private final GetProductUseCase getUseCase;
    private final CreateProductUseCase createUseCase;

    public ProductController(ListProductsUseCase listUseCase, GetProductUseCase getUseCase, CreateProductUseCase createUseCase) {
        this.listUseCase = listUseCase;
        this.getUseCase = getUseCase;
        this.createUseCase = createUseCase;
    }

    @GetMapping
    public List<ProductResponse> list() {
        return listUseCase.execute().stream().map(ProductResponse::from).toList();
    }

    @GetMapping("/{id}")
    public ProductResponse get(@PathVariable UUID id) {
        return ProductResponse.from(getUseCase.execute(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductResponse> create(@Valid @RequestBody CreateProductRequest req) {
        Product product = createUseCase.execute(req.sku(), req.name(), req.description(), req.price(), req.stockQuantity());
        return ResponseEntity.ok(ProductResponse.from(product));
    }

    public record CreateProductRequest(
        @NotBlank String sku,
        @NotBlank String name,
        String description,
        @NotNull @DecimalMin("0.0") BigDecimal price,
        @Min(0) int stockQuantity
    ) {}

    public record ProductResponse(
        String id, String sku, String name, String description,
        BigDecimal price, int stockQuantity, boolean active
    ) {
        public static ProductResponse from(Product p) {
            return new ProductResponse(
                p.getId().toString(), p.getSku(), p.getName(), p.getDescription(),
                p.getPrice(), p.getStockQuantity(), p.isActive()
            );
        }
    }
}
