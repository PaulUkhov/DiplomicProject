package org.example.diplomicproject.service;

import lombok.RequiredArgsConstructor;
import org.example.diplomicproject.domain.Product;
import org.example.diplomicproject.domain.Review;
import org.example.diplomicproject.domain.User;
import org.example.diplomicproject.repository.ReviewRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

    // Получить все отзывы
    public List<Review> findAll() {
        return reviewRepository.findAll();
    }

    // Найти отзыв по ID
    public Optional<Review> findById(Long id) {
        return reviewRepository.findById(id);
    }

    // Создать или обновить отзыв
    public Review save(Review review) {
        return reviewRepository.save(review);
    }

    // Удалить отзыв по ID
    public void deleteById(Long id) {
        reviewRepository.deleteById(id);
    }

    // Получить все отзывы по продукту
    public List<Review> findByProduct(Product product) {
        return reviewRepository.findByProduct(product);
    }

    // Получить все отзывы по пользователю
    public List<Review> findByUser(User user) {
        return reviewRepository.findByUser(user);
    }
}
