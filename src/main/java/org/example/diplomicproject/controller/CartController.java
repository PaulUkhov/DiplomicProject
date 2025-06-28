package org.example.diplomicproject.controller;

import lombok.RequiredArgsConstructor;
import org.example.diplomicproject.domain.Cart;
import org.example.diplomicproject.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    // Получить корзину пользователя по userId
    @GetMapping("/{userId}")
    public ResponseEntity<Cart> getCart(@PathVariable Long userId) {
        Cart cart = cartService.getCartByUserId(userId);
        return ResponseEntity.ok(cart);
    }

    // Добавить продукт в корзину
    @PostMapping("/{userId}/add")
    public ResponseEntity<Void> addProductToCart(
            @PathVariable Long userId,
            @RequestParam Long productId,
            @RequestParam int quantity) {
        cartService.addProductToCart(userId, productId, quantity);
        return ResponseEntity.ok().build();
    }

    // Удалить продукт из корзины
    @DeleteMapping("/{userId}/remove")
    public ResponseEntity<Void> removeProductFromCart(
            @PathVariable Long userId,
            @RequestParam Long productId) {
        cartService.removeProductFromCart(userId, productId);
        return ResponseEntity.noContent().build();
    }

    // Очистить корзину пользователя
    @DeleteMapping("/{userId}/clear")
    public ResponseEntity<Void> clearCart(@PathVariable Long userId) {
        cartService.clearCart(userId);
        return ResponseEntity.noContent().build();
    }
}
