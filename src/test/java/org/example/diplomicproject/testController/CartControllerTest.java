package org.example.diplomicproject.testController;

import org.example.diplomicproject.controller.CartController;
import org.example.diplomicproject.domain.Cart;
import org.example.diplomicproject.security.JwtAuthenticationFilter;
import org.example.diplomicproject.service.CartService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CartController.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthenticationFilter.class))

@AutoConfigureMockMvc(addFilters = false)
class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CartService cartService;

    @Test
    @WithMockUser
    void testGetCart() throws Exception {
        Long userId = 1L;
        Cart cart = new Cart();

        given(cartService.getCartByUserId(userId)).willReturn(cart);

        mockMvc.perform(get("/api/cart/{userId}", userId))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void testAddProductToCart() throws Exception {
        Long userId = 1L;
        Long productId = 100L;
        int quantity = 2;

        doNothing().when(cartService).addProductToCart(userId, productId, quantity);

        mockMvc.perform(post("/api/cart/{userId}/add", userId)
                        .param("productId", productId.toString())
                        .param("quantity", String.valueOf(quantity))
                        .with(csrf()))  // <--- добавляем csrf токен
                .andExpect(status().isOk());

    }

    @Test
    @WithMockUser
    void testRemoveProductFromCart() throws Exception {
        Long userId = 1L;
        Long productId = 100L;

        doNothing().when(cartService).removeProductFromCart(userId, productId);

        mockMvc.perform(delete("/api/cart/{userId}/remove", userId)
                        .param("productId", productId.toString())
                        .with(csrf()))
                .andExpect(status().isNoContent());

    }

    @Test
    @WithMockUser
    void testClearCart() throws Exception {
        Long userId = 1L;

        doNothing().when(cartService).clearCart(userId);

        mockMvc.perform(delete("/api/cart/{userId}/clear", userId)
                .with(csrf()))
                .andExpect(status().isNoContent());
    }
}
