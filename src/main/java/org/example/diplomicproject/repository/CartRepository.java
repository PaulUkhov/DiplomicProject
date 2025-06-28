package org.example.diplomicproject.repository;

import org.example.diplomicproject.domain.Cart;
import org.example.diplomicproject.domain.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUserId(Long userId);
    Optional<Cart> findByTotalPrice(BigDecimal totalPrice);

}
