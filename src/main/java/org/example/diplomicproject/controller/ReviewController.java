package org.example.diplomicproject.controller;

import lombok.RequiredArgsConstructor;
import org.example.diplomicproject.domain.Product;
import org.example.diplomicproject.domain.Review;
import org.example.diplomicproject.domain.User;
import org.example.diplomicproject.service.ReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    // Получить все отзывы
    @GetMapping
    public ResponseEntity<List<Review>> getAllReviews() {
        return ResponseEntity.ok(reviewService.findAll());
    }

    // Получить отзыв по ID
    @GetMapping("/{id}")
    public ResponseEntity<Review> getReviewById(@PathVariable Long id) {
        return reviewService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Создать или обновить отзыв
    @PostMapping
    public ResponseEntity<Review> saveReview(@RequestBody Review review) {
        Review savedReview = reviewService.save(review);
        return ResponseEntity.ok(savedReview);
    }

    // Удалить отзыв по ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
        reviewService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // Получить все отзывы по продукту (по productId)
    @GetMapping("/by-product/{productId}")
    public ResponseEntity<List<Review>> getReviewsByProduct(@PathVariable Long productId) {
        // Тут нужен сервис для поиска продукта, или передать в сервис product по id,
        // для примера — просто заглушка:
        Product product = new Product();
        product.setId(productId);
        List<Review> reviews = reviewService.findByProduct(product);
        return ResponseEntity.ok(reviews);
    }

    // Получить все отзывы по пользователю (по userId)
    @GetMapping("/by-user/{userId}")
    public ResponseEntity<List<Review>> getReviewsByUser(@PathVariable Long userId) {
        User user = new User();
        user.setId(userId);
        List<Review> reviews = reviewService.findByUser(user);
        return ResponseEntity.ok(reviews);
    }
}
