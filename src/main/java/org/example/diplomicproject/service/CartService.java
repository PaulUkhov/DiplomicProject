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
        log.info("Получение корзины для пользователя с ID: {}", userId);
        return cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    log.info("Корзина для пользователя с ID {} не найдена. Создание новой.", userId);
                    return createCartForUser(userId);
                });
    }

    // Добавить продукт в корзину
    public void addProductToCart(Long userId, Long productId, int quantity) {
        log.info("Добавление продукта ID {} в количестве {} в корзину пользователя ID {}", productId, quantity, userId);

        Cart cart = getCartByUserId(userId);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> {
                    log.warn("Продукт с ID {} не найден", productId);
                    return new RuntimeException("Продукт не найден");
                });

        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst();

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + quantity);
            log.debug("Увеличено количество существующего товара ID {} в корзине", productId);
        } else {
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setProduct(product);
            newItem.setQuantity(quantity);
            newItem.setPriceAtAdd(product.getPrice());
            cart.getItems().add(newItem);
            log.debug("Добавлен новый товар ID {} в корзину", productId);
        }

        recalculateTotal(cart);
        cartRepository.save(cart);
        log.info("Корзина пользователя ID {} обновлена", userId);
    }

    // Удалить продукт из корзины
    public void removeProductFromCart(Long userId, Long productId) {
        log.info("Удаление продукта ID {} из корзины пользователя ID {}", productId, userId);

        Cart cart = getCartByUserId(userId);
        boolean removed = cart.getItems().removeIf(item -> item.getProduct().getId().equals(productId));
        if (removed) {
            log.debug("Продукт ID {} удалён из корзины", productId);
        } else {
            log.warn("Продукт ID {} не найден в корзине", productId);
        }

        recalculateTotal(cart);
        cartRepository.save(cart);
        log.info("Корзина пользователя ID {} обновлена после удаления", userId);
    }

    // Очистить корзину
    public void clearCart(Long userId) {
        log.info("Очистка корзины пользователя ID {}", userId);

        Cart cart = getCartByUserId(userId);
        cart.getItems().clear();
        cart.setTotalPrice(BigDecimal.ZERO);
        cartRepository.save(cart);

        log.info("Корзина пользователя ID {} очищена", userId);
    }

    // Пересчитать сумму корзины
    private void recalculateTotal(Cart cart) {
        BigDecimal total = cart.getItems().stream()
                .map(item -> item.getPriceAtAdd().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        cart.setTotalPrice(total);

        log.debug("Пересчитана сумма корзины. Новая сумма: {}", total);
    }

    // Создать корзину для пользователя (если нет)
    private Cart createCartForUser(Long userId) {
        log.info("Создание корзины для пользователя ID {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("Пользователь с ID {} не найден", userId);
                    return new RuntimeException("Пользователь не найден");
                });

        Cart cart = new Cart();
        cart.setUser(user);
        cart.setTotalPrice(BigDecimal.ZERO);
        Cart savedCart = cartRepository.save(cart);

        log.info("Корзина для пользователя ID {} успешно создана", userId);
        return savedCart;
    }
}

