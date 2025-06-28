package org.example.diplomicproject.testService;

import org.example.diplomicproject.domain.Category;
import org.example.diplomicproject.repository.CategoryRepository;
import org.example.diplomicproject.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    private Category category;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        category = new Category();
        category.setId(1L);
        category.setName("TestCategory");
    }

    @Test
    void testFindAll() {
        List<Category> categories = List.of(category);
        when(categoryRepository.findAll()).thenReturn(categories);

        List<Category> result = categoryService.findAll();

        assertEquals(1, result.size());
        assertEquals("TestCategory", result.get(0).getName());
        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    void testFindById() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        Optional<Category> result = categoryService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals("TestCategory", result.get().getName());
        verify(categoryRepository, times(1)).findById(1L);
    }

    @Test
    void testSave() {
        when(categoryRepository.save(category)).thenReturn(category);

        Category saved = categoryService.save(category);

        assertNotNull(saved);
        assertEquals("TestCategory", saved.getName());
        verify(categoryRepository, times(1)).save(category);
    }

    @Test
    void testDeleteById() {
        doNothing().when(categoryRepository).deleteById(1L);

        categoryService.deleteById(1L);

        verify(categoryRepository, times(1)).deleteById(1L);
    }


}

