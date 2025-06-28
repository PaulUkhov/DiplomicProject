package org.example.diplomicproject.repository;

import org.example.diplomicproject.domain.Product;
import org.example.diplomicproject.domain.Review;
import org.example.diplomicproject.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByProduct(Product product);

    List<Review> findByUser(User user);
    Optional<Review> findByRating(int rating);
    Optional<Review> findByComment(String comment);
}
