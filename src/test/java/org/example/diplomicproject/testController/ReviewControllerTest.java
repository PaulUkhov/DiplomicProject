package org.example.diplomicproject.testController;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.diplomicproject.controller.ReviewController;
import org.example.diplomicproject.domain.Product;
import org.example.diplomicproject.domain.Review;
import org.example.diplomicproject.domain.User;
import org.example.diplomicproject.security.JwtAuthenticationFilter;
import org.example.diplomicproject.service.ReviewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ReviewController.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthenticationFilter.class))

@AutoConfigureMockMvc(addFilters = false)
public class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReviewService reviewService;

    @Autowired
    private ObjectMapper objectMapper;

    private Review review;
    private Product product;
    private User user;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setId(10L);

        user = new User();
        user.setId(20L);

        review = new Review();
        review.setId(1L);
        review.setProduct(product);
        review.setUser(user);
    }

    @Test
    @WithMockUser
    void testGetAllReviews() throws Exception {
        Mockito.when(reviewService.findAll()).thenReturn(List.of(review));

        mockMvc.perform(get("/api/reviews"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(review.getId().intValue())));

    }

    @Test
    @WithMockUser
    void testGetReviewById_found() throws Exception {
        Mockito.when(reviewService.findById(1L)).thenReturn(Optional.of(review));

        mockMvc.perform(get("/api/reviews/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(review.getId().intValue())));

    }

    @Test
    @WithMockUser
    void testGetReviewById_notFound() throws Exception {
        Mockito.when(reviewService.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/reviews/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void testSaveReview() throws Exception {
        Mockito.when(reviewService.save(any(Review.class))).thenReturn(review);

        mockMvc.perform(post("/api/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(review))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(review.getId().intValue())));

    }

    @Test
    @WithMockUser
    void testDeleteReview() throws Exception {
        Mockito.doNothing().when(reviewService).deleteById(1L);

        mockMvc.perform(delete("/api/reviews/1").with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser
    void testGetReviewsByProduct() throws Exception {
        Mockito.when(reviewService.findByProduct(any(Product.class))).thenReturn(List.of(review));

        mockMvc.perform(get("/api/reviews/by-product/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(review.getId().intValue())));
    }

    @Test
    @WithMockUser
    void testGetReviewsByUser() throws Exception {
        Mockito.when(reviewService.findByUser(any(User.class))).thenReturn(List.of(review));

        mockMvc.perform(get("/api/reviews/by-user/20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(review.getId().intValue())));
    }
}
