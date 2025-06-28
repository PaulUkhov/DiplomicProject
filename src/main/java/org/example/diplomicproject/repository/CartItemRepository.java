package org.example.diplomicproject.repository;


import org.example.diplomicproject.domain.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByQuantity(Integer quantity);
    Optional<CartItem> findByPriceAtAdd(BigDecimal price);
}
