package org.example.diplomicproject.controller;

import lombok.RequiredArgsConstructor;
import org.example.diplomicproject.domain.Cart;
import org.example.diplomicproject.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;


@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@Tag(name = "Корзина", description = "Операции с корзиной пользователя")
public class CartController {

    private final CartService cartService;

    @Operation(summary = "Получить корзину пользователя", description = "Возвращает корзину по ID пользователя")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Корзина найдена"),
            @ApiResponse(responseCode = "404", description = "Корзина не найдена")
    })
    @GetMapping("/{userId}")
    public ResponseEntity<Cart> getCart(
            @Parameter(description = "ID пользователя") @PathVariable Long userId) {
        Cart cart = cartService.getCartByUserId(userId);
        return ResponseEntity.ok(cart);
    }

    @Operation(summary = "Добавить продукт в корзину", description = "Добавляет товар и его количество в корзину пользователя")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Продукт добавлен в корзину"),
            @ApiResponse(responseCode = "400", description = "Неверные параметры запроса"),
            @ApiResponse(responseCode = "404", description = "Пользователь или продукт не найден")
    })
    @PostMapping("/{userId}/add")
    public ResponseEntity<Void> addProductToCart(
            @Parameter(description = "ID пользователя") @PathVariable Long userId,
            @Parameter(description = "ID продукта") @RequestParam Long productId,
            @Parameter(description = "Количество товара") @RequestParam int quantity) {
        cartService.addProductToCart(userId, productId, quantity);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Удалить продукт из корзины", description = "Удаляет товар из корзины пользователя")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Продукт удалён"),
            @ApiResponse(responseCode = "404", description = "Пользователь или продукт не найден")
    })
    @DeleteMapping("/{userId}/remove")
    public ResponseEntity<Void> removeProductFromCart(
            @Parameter(description = "ID пользователя") @PathVariable Long userId,
            @Parameter(description = "ID продукта") @RequestParam Long productId) {
        cartService.removeProductFromCart(userId, productId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Очистить корзину", description = "Удаляет все товары из корзины пользователя")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Корзина очищена"),
            @ApiResponse(responseCode = "404", description = "Корзина не найдена")
    })
    @DeleteMapping("/{userId}/clear")
    public ResponseEntity<Void> clearCart(
            @Parameter(description = "ID пользователя") @PathVariable Long userId) {
        cartService.clearCart(userId);
        return ResponseEntity.noContent().build();
    }
}
