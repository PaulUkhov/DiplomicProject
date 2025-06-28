package org.example.diplomicproject.controller;

import lombok.RequiredArgsConstructor;
import org.example.diplomicproject.domain.OrderStatus;
import org.example.diplomicproject.domain.Order;
import org.example.diplomicproject.dto.OrderRequest;
import org.example.diplomicproject.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    // Получить все заказы
    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        return ResponseEntity.ok(orderService.findAll());
    }

    // Получить заказ по ID
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        return orderService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Создать или обновить заказ
    @PostMapping("/api/orders")
    public ResponseEntity<Order> createOrUpdateOrder(@RequestBody Order order) {
        Order savedOrder = orderService.save(order);
        return ResponseEntity.ok(savedOrder);
    }

    // Удалить заказ по ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        if (!orderService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        orderService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // Обновить статус заказа
    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> updateOrderStatus(@PathVariable Long id, @RequestParam OrderStatus status) {
        try {
            orderService.updateStatus(id, status);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    @PostMapping("/api/orders/update")
    public ResponseEntity<String> createOrder(@RequestBody OrderRequest orderRequest) {
        try {
            orderService.processOrder(orderRequest);
            return ResponseEntity.ok("Заказ успешно оформлен");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Ошибка оформления заказа: " + e.getMessage());
        }
    }
}
