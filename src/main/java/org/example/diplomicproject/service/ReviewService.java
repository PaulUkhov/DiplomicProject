package org.example.diplomicproject.service;

import lombok.RequiredArgsConstructor;
import org.example.diplomicproject.domain.Product;
import org.example.diplomicproject.domain.Review;
import org.example.diplomicproject.domain.User;
import org.example.diplomicproject.repository.ReviewRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import lombok.extern.slf4j.Slf4j;



@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

    // Получить все отзывы
    public List<Review> findAll() {
        log.info("Получение всех отзывов");
        List<Review> reviews = reviewRepository.findAll();
        log.debug("Найдено {} отзывов", reviews.size());
        return reviews;
    }

    // Найти отзыв по ID
    public Optional<Review> findById(Long id) {
        log.info("Поиск отзыва по ID: {}", id);
        Optional<Review> review = reviewRepository.findById(id);
        if (review.isPresent()) {
            log.debug("Отзыв найден: ID = {}, рейтинг = {}", review.get().getId(), review.get().getRating());
        } else {
            log.warn("Отзыв с ID {} не найден", id);
        }
        return review;
    }

    // Создать или обновить отзыв
    public Review save(Review review) {
        boolean isNew = review.getId() == null;
        Review saved = reviewRepository.save(review);
        log.info("{} отзыв: ID = {}, рейтинг = {}, продукт = {}, пользователь = {}",
                isNew ? "Создан" : "Обновлён",
                saved.getId(),
                saved.getRating(),
                saved.getProduct().getName(),
                saved.getUser().getLogin());
        return saved;
    }

    // Удалить отзыв по ID
    public void deleteById(Long id) {
        log.info("Удаление отзыва с ID: {}", id);
        reviewRepository.deleteById(id);
        log.debug("Отзыв с ID {} удалён", id);
    }

    // Получить все отзывы по продукту
    public List<Review> findByProduct(Product product) {
        log.info("Поиск отзывов по продукту: {}", product.getName());
        List<Review> reviews = reviewRepository.findByProduct(product);
        log.debug("Найдено {} отзывов для продукта {}", reviews.size(), product.getName());
        return reviews;
    }

    // Получить все отзывы по пользователю
    public List<Review> findByUser(User user) {
        log.info("Поиск отзывов пользователя: {}", user.getLogin());
        List<Review> reviews = reviewRepository.findByUser(user);
        log.debug("Найдено {} отзывов пользователя {}", reviews.size(), user.getLogin());
        return reviews;
    }
}
