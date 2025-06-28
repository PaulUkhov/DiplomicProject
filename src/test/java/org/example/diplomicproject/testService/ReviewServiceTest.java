package org.example.diplomicproject.testService;

import org.example.diplomicproject.domain.Product;
import org.example.diplomicproject.domain.Review;
import org.example.diplomicproject.domain.User;
import org.example.diplomicproject.repository.ReviewRepository;
import org.example.diplomicproject.service.ReviewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @InjectMocks
    private ReviewService reviewService;

    private Review review;
    private Product product;
    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        product = new Product();
        product.setId(1L);

        user = new User();
        user.setId(1L);

        review = new Review();
        review.setId(1L);
        review.setProduct(product);
        review.setUser(user);

    }

    @Test
    void testFindAll() {
        List<Review> reviews = List.of(review);
        when(reviewRepository.findAll()).thenReturn(reviews);

        List<Review> result = reviewService.findAll();

        assertEquals(1, result.size());
        verify(reviewRepository, times(1)).findAll();
    }

    @Test
    void testFindById() {
        when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));

        Optional<Review> result = reviewService.findById(1L);

        assertTrue(result.isPresent());
        verify(reviewRepository, times(1)).findById(1L);
    }

    @Test
    void testSave() {
        when(reviewRepository.save(review)).thenReturn(review);

        Review saved = reviewService.save(review);

        assertNotNull(saved);
        verify(reviewRepository, times(1)).save(review);
    }

    @Test
    void testDeleteById() {
        doNothing().when(reviewRepository).deleteById(1L);

        reviewService.deleteById(1L);

        verify(reviewRepository, times(1)).deleteById(1L);
    }

    @Test
    void testFindByProduct() {
        List<Review> reviews = List.of(review);
        when(reviewRepository.findByProduct(product)).thenReturn(reviews);

        List<Review> result = reviewService.findByProduct(product);

        assertEquals(1, result.size());
        assertEquals(product, result.get(0).getProduct());
        verify(reviewRepository, times(1)).findByProduct(product);
    }

    @Test
    void testFindByUser() {
        List<Review> reviews = List.of(review);
        when(reviewRepository.findByUser(user)).thenReturn(reviews);

        List<Review> result = reviewService.findByUser(user);

        assertEquals(1, result.size());
        assertEquals(user, result.get(0).getUser());
        verify(reviewRepository, times(1)).findByUser(user);
    }
}
