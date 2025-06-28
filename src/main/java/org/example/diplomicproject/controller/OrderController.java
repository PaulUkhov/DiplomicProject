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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Tag(name = "Заказы", description = "Управление заказами")
public class OrderController {

    private final OrderService orderService;

    @Operation(summary = "Получить все заказы", description = "Возвращает список всех заказов")
    @ApiResponse(responseCode = "200", description = "Список заказов",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Order.class)))
    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        return ResponseEntity.ok(orderService.findAll());
    }

    @Operation(summary = "Получить заказ по ID", description = "Возвращает заказ по указанному ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Заказ найден"),
            @ApiResponse(responseCode = "404", description = "Заказ не найден")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(
            @Parameter(description = "ID заказа") @PathVariable Long id) {
        return orderService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Создать или обновить заказ", description = "Сохраняет новый заказ или обновляет существующий")
    @ApiResponse(responseCode = "200", description = "Заказ сохранён")
    @PostMapping
    public ResponseEntity<Order> createOrUpdateOrder(
            @Parameter(description = "Данные заказа") @RequestBody Order order) {
        Order savedOrder = orderService.save(order);
        return ResponseEntity.ok(savedOrder);
    }

    @Operation(summary = "Удалить заказ", description = "Удаляет заказ по ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Заказ удалён"),
            @ApiResponse(responseCode = "404", description = "Заказ не найден")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(
            @Parameter(description = "ID заказа") @PathVariable Long id) {
        if (!orderService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        orderService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Обновить статус заказа", description = "Обновляет статус заказа по ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Статус обновлён"),
            @ApiResponse(responseCode = "404", description = "Заказ не найден")
    })
    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> updateOrderStatus(
            @Parameter(description = "ID заказа") @PathVariable Long id,
            @Parameter(description = "Новый статус заказа") @RequestParam OrderStatus status) {
        try {
            orderService.updateStatus(id, status);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Оформить заказ", description = "Создаёт новый заказ на основе данных запроса")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Заказ успешно оформлен"),
            @ApiResponse(responseCode = "400", description = "Ошибка оформления заказа")
    })
    @PostMapping("/update")
    public ResponseEntity<String> createOrder(
            @Parameter(description = "Данные заказа") @RequestBody OrderRequest orderRequest) {
        try {
            orderService.processOrder(orderRequest);
            return ResponseEntity.ok("Заказ успешно оформлен");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Ошибка оформления заказа: " + e.getMessage());
        }
    }
}

