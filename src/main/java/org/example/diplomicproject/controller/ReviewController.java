package org.example.diplomicproject.controller;

import lombok.RequiredArgsConstructor;
import org.example.diplomicproject.domain.Product;
import org.example.diplomicproject.domain.Review;
import org.example.diplomicproject.domain.User;
import org.example.diplomicproject.service.ReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
@Tag(name = "Отзывы", description = "Операции с отзывами пользователей на продукты")
public class ReviewController {

    private final ReviewService reviewService;

    @Operation(summary = "Получить все отзывы")
    @ApiResponse(responseCode = "200", description = "Список отзывов получен",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Review.class)))
    @GetMapping
    public ResponseEntity<List<Review>> getAllReviews() {
        return ResponseEntity.ok(reviewService.findAll());
    }

    @Operation(summary = "Получить отзыв по ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Отзыв найден"),
            @ApiResponse(responseCode = "404", description = "Отзыв не найден")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Review> getReviewById(
            @Parameter(description = "ID отзыва") @PathVariable Long id) {
        return reviewService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Создать или обновить отзыв")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Отзыв сохранён"),
            @ApiResponse(responseCode = "400", description = "Ошибка валидации данных")
    })
    @PostMapping
    public ResponseEntity<Review> saveReview(
            @RequestBody @Parameter(description = "Тело запроса с данными отзыва") Review review) {
        Review savedReview = reviewService.save(review);
        return ResponseEntity.ok(savedReview);
    }

    @Operation(summary = "Удалить отзыв по ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Отзыв удалён"),
            @ApiResponse(responseCode = "404", description = "Отзыв не найден")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(
            @Parameter(description = "ID отзыва для удаления") @PathVariable Long id) {
        reviewService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Получить все отзывы по продукту")
    @ApiResponse(responseCode = "200", description = "Отзывы по продукту найдены")
    @GetMapping("/by-product/{productId}")
    public ResponseEntity<List<Review>> getReviewsByProduct(
            @Parameter(description = "ID продукта") @PathVariable Long productId) {
        Product product = new Product();
        product.setId(productId);
        List<Review> reviews = reviewService.findByProduct(product);
        return ResponseEntity.ok(reviews);
    }

    @Operation(summary = "Получить все отзывы пользователя")
    @ApiResponse(responseCode = "200", description = "Отзывы пользователя найдены")
    @GetMapping("/by-user/{userId}")
    public ResponseEntity<List<Review>> getReviewsByUser(
            @Parameter(description = "ID пользователя") @PathVariable Long userId) {
        User user = new User();
        user.setId(userId);
        List<Review> reviews = reviewService.findByUser(user);
        return ResponseEntity.ok(reviews);
    }
}
