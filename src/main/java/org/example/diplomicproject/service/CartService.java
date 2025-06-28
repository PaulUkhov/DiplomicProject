package org.example.diplomicproject.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.diplomicproject.domain.Cart;
import org.example.diplomicproject.domain.CartItem;
import org.example.diplomicproject.domain.Product;
import org.example.diplomicproject.domain.User;
import org.example.diplomicproject.repository.CartItemRepository;
import org.example.diplomicproject.repository.CartRepository;
import org.example.diplomicproject.repository.ProductRepository;
import org.example.diplomicproject.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;
@Slf4j
@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;

    // Получить корзину по ID пользователя
    public Cart getCartByUserId(Long userId) {
        return cartRepository.findByUserId(userId)
                .orElseGet(() -> createCartForUser(userId));
    }

    // Добавить продукт в корзину
    public void addProductToCart(Long userId, Long productId, int quantity) {
        Cart cart = getCartByUserId(userId);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Продукт не найден"));

        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst();

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + quantity);
        } else {
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setProduct(product);
            newItem.setQuantity(quantity);
            newItem.setPriceAtAdd(product.getPrice());
            cart.getItems().add(newItem);
        }

        recalculateTotal(cart);
        cartRepository.save(cart);
    }

    // Удалить продукт из корзины
    public void removeProductFromCart(Long userId, Long productId) {
        Cart cart = getCartByUserId(userId);
        cart.getItems().removeIf(item -> item.getProduct().getId().equals(productId));
        recalculateTotal(cart);
        cartRepository.save(cart);
    }

    // Очистить корзину
    public void clearCart(Long userId) {
        Cart cart = getCartByUserId(userId);
        cart.getItems().clear();
        cart.setTotalPrice(BigDecimal.ZERO);
        cartRepository.save(cart);
    }

    // Пересчитать сумму корзины
    private void recalculateTotal(Cart cart) {
        BigDecimal total = cart.getItems().stream()
                .map(item -> item.getPriceAtAdd().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        cart.setTotalPrice(total);
    }

    // Создать корзину для пользователя (если нет)
    private Cart createCartForUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        Cart cart = new Cart();
        cart.setUser(user);
        cart.setTotalPrice(BigDecimal.ZERO);
        return cartRepository.save(cart);
    }
}
