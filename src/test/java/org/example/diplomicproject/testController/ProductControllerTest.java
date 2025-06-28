package org.example.diplomicproject.testController;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.diplomicproject.controller.ProductController;
import org.example.diplomicproject.domain.Category;
import org.example.diplomicproject.domain.Product;
import org.example.diplomicproject.security.JwtAuthenticationFilter;
import org.example.diplomicproject.service.ProductService;
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

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ProductController.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthenticationFilter.class))

@AutoConfigureMockMvc(addFilters = false)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;

    @Test
    @WithMockUser
    void testGetAllProducts() throws Exception {
        Product product = new Product(1L, "Test Name", "Test Product", null, null, new Category(), List.of());
        given(productService.findAll()).willReturn(List.of(product));

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Test Name"))
                .andExpect(jsonPath("$[0].description").value("Test Product"));
    }

    @Test
    @WithMockUser
    void testGetProductById_found() throws Exception {
        Product product = new Product(1L, "Test Name", "Test Product", null, null, new Category(), List.of());
        given(productService.findById(1L)).willReturn(Optional.of(product));

        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test Name"))
                .andExpect(jsonPath("$.description").value("Test Product"));
    }

    @Test
    @WithMockUser
    void testGetProductById_notFound() throws Exception {
        given(productService.findById(1L)).willReturn(Optional.empty());

        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void testCreateProduct() throws Exception {
        Product product = new Product(1L, "Test Name", "Test Product", null, null, new Category(), List.of());
        Product saved = new Product(1L, "Test Name", "Test Product", null, null, new Category(), List.of());
        given(productService.save(Mockito.any(Product.class))).willReturn(saved);

        mockMvc.perform(post("/api/products")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test Name"));
    }


    @Test
    @WithMockUser
    void testUpdateProduct_found() throws Exception {
        Product updated = new Product(1L, "Updated Product", "Test Product", null, null, new Category(), List.of());
        given(productService.existsById(1L)).willReturn(true);
        given(productService.save(Mockito.any(Product.class))).willReturn(updated);

        mockMvc.perform(put("/api/products/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Product"));
    }

    @Test
    @WithMockUser
    void testUpdateProduct_notFound() throws Exception {
        Product product = new Product(1L, "Test name", "Test Product", null, null, new Category(), List.of());
        given(productService.existsById(1L)).willReturn(false);

        mockMvc.perform(put("/api/products/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void testDeleteProduct_found() throws Exception {
        given(productService.existsById(1L)).willReturn(true);
        doNothing().when(productService).deleteById(1L);

        mockMvc.perform(delete("/api/products/1").with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser
    void testDeleteProduct_notFound() throws Exception {
        given(productService.existsById(1L)).willReturn(false);

        mockMvc.perform(delete("/api/products/1").with(csrf()))
                .andExpect(status().isNotFound());
    }
}
