package org.example.diplomicproject.testController;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.diplomicproject.controller.CategoryController;
import org.example.diplomicproject.domain.Category;
import org.example.diplomicproject.security.JwtAuthenticationFilter;
import org.example.diplomicproject.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(controllers = CategoryController.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthenticationFilter.class))

@AutoConfigureMockMvc(addFilters = false)
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    void testGetAllCategories() throws Exception {
        List<Category> categories = List.of(
                new Category(1L, "Books", List.of()),
                new Category(2L, "Electronics", List.of())
        );

        given(categoryService.findAll()).willReturn(categories);

        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Books"))
                .andExpect(jsonPath("$[1].name").value("Electronics"));
    }

    @Test
    @WithMockUser
    void testGetCategoryById_found() throws Exception {
        Category category = new Category(1L, "Books", List.of());

        given(categoryService.findById(1L)).willReturn(Optional.of(category));

        mockMvc.perform(get("/api/categories/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Books"));
    }

    @Test
    @WithMockUser
    void testGetCategoryById_notFound() throws Exception {
        given(categoryService.findById(1L)).willReturn(Optional.empty());

        mockMvc.perform(get("/api/categories/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void testCreateCategory_success() throws Exception {
        Category input = new Category(null, "Books", null);
        Category saved = new Category(1L, "Books", List.of());

        given(categoryService.existsByName("Books")).willReturn(Optional.empty());
        given(categoryService.save(any(Category.class))).willReturn(saved);

        mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input))
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Books"));
    }

    @Test
    @WithMockUser
    void testCreateCategory_conflict() throws Exception {
        Category input = new Category(null, "Books", null);

        given(categoryService.existsByName("Books")).willReturn(Optional.of(new Category(99L, "Books", List.of())));

        mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input))
                        .with(csrf()))
                .andExpect(status().isConflict())
                .andExpect(content().string("Категория с таким именем уже существует"));
    }

    @Test
    @WithMockUser
    void testUpdateCategory_success() throws Exception {
        Category existing = new Category(1L, "OldName", List.of());
        Category updated = new Category(1L, "NewName", List.of());

        given(categoryService.findById(1L)).willReturn(Optional.of(existing));
        given(categoryService.save(any(Category.class))).willReturn(updated);

        mockMvc.perform(put("/api/categories/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("NewName"));
    }

    @Test
    @WithMockUser
    void testUpdateCategory_notFound() throws Exception {
        Category update = new Category(1L, "NewName", null);

        given(categoryService.findById(1L)).willReturn(Optional.empty());

        mockMvc.perform(put("/api/categories/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update))
                        .with(csrf()))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void testDeleteCategory_success() throws Exception {
        given(categoryService.findById(1L)).willReturn(Optional.of(new Category(1L, "Books", List.of())));

        mockMvc.perform(delete("/api/categories/1").with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser
    void testDeleteCategory_notFound() throws Exception {
        given(categoryService.findById(1L)).willReturn(Optional.empty());

        mockMvc.perform(delete("/api/categories/1").with(csrf()))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Категория не найдена"));
    }
}

