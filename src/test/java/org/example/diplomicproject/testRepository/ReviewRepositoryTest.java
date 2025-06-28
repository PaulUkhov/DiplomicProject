package org.example.diplomicproject.testRepository;

import org.example.diplomicproject.domain.Category;
import org.example.diplomicproject.domain.Product;
import org.example.diplomicproject.domain.Review;
import org.example.diplomicproject.domain.User;
import org.example.diplomicproject.repository.CategoryRepository;
import org.example.diplomicproject.repository.ProductRepository;
import org.example.diplomicproject.repository.ReviewRepository;
import org.example.diplomicproject.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.assertj.core.api.Assertions.assertThat;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@DataJpaTest
public class ReviewRepositoryTest {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void testSaveAndFindById() {
        User user = createTestUser();
        Product product = createTestProduct();

        Review review = new Review();
        review.setUser(user);
        review.setProduct(product);
        review.setRating(5);
        review.setComment("Отличный товар!");

        // Явно задаем даты, если не используется аудирование
        review.setCreatedDate(LocalDateTime.now());
        review.setLastModifiedDate(LocalDateTime.now());

        review = reviewRepository.save(review);

        Optional<Review> found = reviewRepository.findById(review.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getRating()).isEqualTo(5);
    }

    @Test
    void testFindByProduct() {
        User user = createTestUser();
        Product product = createTestProduct();

        Review review = new Review();
        review.setUser(user);
        review.setProduct(product);
        review.setRating(4);
        review.setComment("Нормально");

        // Явно задаём даты
        review.setCreatedDate(LocalDateTime.now());
        review.setLastModifiedDate(LocalDateTime.now());

        reviewRepository.save(review);

        List<Review> reviews = reviewRepository.findByProduct(product);
        assertThat(reviews).isNotEmpty();
        assertThat(reviews.get(0).getProduct().getId()).isEqualTo(product.getId());
    }

    @Test
    void testFindByUser() {
        User user = createTestUser();
        Product product = createTestProduct();

        Review review = new Review();
        review.setUser(user);
        review.setProduct(product);
        review.setRating(3);
        review.setComment("Так себе");
        review.setCreatedDate(LocalDateTime.now());
        review.setLastModifiedDate(LocalDateTime.now());
        reviewRepository.save(review);

        List<Review> reviews = reviewRepository.findByUser(user);
        assertThat(reviews).isNotEmpty();
        assertThat(reviews.get(0).getUser().getId()).isEqualTo(user.getId());
    }

    // ==== Вспомогательные методы ====

    private User createTestUser() {
        User user = new User();
        user.setLogin("reviewer");
        user.setEmail("reviewer@example.com");
        return userRepository.save(user);
    }

    private Product createTestProduct() {
        Category category = new Category();
        category.setName("Test Category");
        category = categoryRepository.save(category);

        Product product = new Product();
        product.setName("Test Product");
        product.setDescription("Описание");
        product.setPrice(BigDecimal.valueOf(10.0));
        product.setCategory(category);
        return productRepository.save(product);
    }
}
