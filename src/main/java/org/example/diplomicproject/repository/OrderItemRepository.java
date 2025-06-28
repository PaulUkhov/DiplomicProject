package org.example.diplomicproject.repository;

import org.example.diplomicproject.domain.Order;
import org.example.diplomicproject.domain.OrderItem;
import org.example.diplomicproject.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    // Найти по заказу
    List<OrderItem> findByOrder(Order order);

    // Найти по продукту
    List<OrderItem> findByProduct(Product product);

    // Найти по количеству (если нужно тестировать конкретное поле)
    List<OrderItem> findByQuantity(int quantity);

    // Найти по цене покупки
    List<OrderItem> findByPriceAtPurchase(BigDecimal priceAtPurchase);

    // Можно также комбинированные запросы:
    Optional<OrderItem> findByOrderAndProduct(Order order, Product product);
}
