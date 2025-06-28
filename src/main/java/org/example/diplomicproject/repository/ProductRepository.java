package org.example.diplomicproject.repository;

import org.example.diplomicproject.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByName(String name);
    Optional<Product> findByDescription(String description);
    Optional<Product> findByAverageRating(Double averageRating);
    Optional<Product> findByPrice(BigDecimal price);
}
