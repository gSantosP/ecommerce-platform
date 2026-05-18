package com.ecomm.order.presentation;

import com.ecomm.auth.domain.User;
import com.ecomm.order.application.CreateOrderUseCase;
import com.ecomm.order.application.GetOrderUseCase;
import com.ecomm.order.application.ListMyOrdersUseCase;
import com.ecomm.order.domain.Order;
import com.ecomm.order.domain.OrderItem;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final CreateOrderUseCase createUseCase;
    private final ListMyOrdersUseCase listUseCase;
    private final GetOrderUseCase getUseCase;

    public OrderController(CreateOrderUseCase createUseCase, ListMyOrdersUseCase listUseCase, GetOrderUseCase getUseCase) {
        this.createUseCase = createUseCase;
        this.listUseCase = listUseCase;
        this.getUseCase = getUseCase;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> create(
        @Valid @RequestBody CreateOrderRequest req,
        @AuthenticationPrincipal User user
    ) {
        List<CreateOrderUseCase.CreateOrderItem> items = req.items().stream()
            .map(i -> new CreateOrderUseCase.CreateOrderItem(i.productId(), i.quantity()))
            .toList();
        Order order = createUseCase.execute(user.id(), items);
        return ResponseEntity.ok(OrderResponse.from(order));
    }

    @GetMapping
    public List<OrderResponse> listMine(@AuthenticationPrincipal User user) {
        return listUseCase.execute(user.id()).stream().map(OrderResponse::from).toList();
    }

    @GetMapping("/{id}")
    public OrderResponse get(@PathVariable UUID id) {
        return OrderResponse.from(getUseCase.execute(id));
    }

    public record CreateOrderRequest(@NotEmpty List<Item> items) {
        public record Item(@NotNull UUID productId, @Min(1) int quantity) {}
    }

    public record OrderResponse(
        String id,
        String customerId,
        String status,
        BigDecimal totalAmount,
        List<OrderItemResponse> items,
        String createdAt
    ) {
        public static OrderResponse from(Order o) {
            return new OrderResponse(
                o.getId().toString(),
                o.getCustomerId().toString(),
                o.getStatus().name(),
                o.getTotalAmount(),
                o.getItems().stream().map(OrderItemResponse::from).toList(),
                o.getCreatedAt().toString()
            );
        }
    }

    public record OrderItemResponse(
        String productId, String productName, int quantity, BigDecimal unitPrice, BigDecimal subtotal
    ) {
        public static OrderItemResponse from(OrderItem i) {
            return new OrderItemResponse(
                i.productId().toString(), i.productName(), i.quantity(), i.unitPrice(), i.subtotal()
            );
        }
    }
}
