package org.example.diplomicproject.testService;

import org.example.diplomicproject.domain.Product;
import org.example.diplomicproject.repository.ProductRepository;
import org.example.diplomicproject.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product product;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        product = new Product();
        product.setId(1L);
        product.setName("Test Product");
    }

    @Test
    void testFindAll() {
        List<Product> products = List.of(product);
        when(productRepository.findAll()).thenReturn(products);

        List<Product> result = productService.findAll();

        assertEquals(1, result.size());
        assertEquals(product.getName(), result.get(0).getName());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void testFindById() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        Optional<Product> result = productService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(product.getName(), result.get().getName());
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    void testSave() {
        when(productRepository.save(product)).thenReturn(product);

        Product saved = productService.save(product);

        assertNotNull(saved);
        assertEquals(product.getName(), saved.getName());
        verify(productRepository, times(1)).save(product);
    }

    @Test
    void testDeleteById() {
        doNothing().when(productRepository).deleteById(1L);

        productService.deleteById(1L);

        verify(productRepository, times(1)).deleteById(1L);
    }

    @Test
    void testExistsById() {
        when(productRepository.existsById(1L)).thenReturn(true);

        boolean exists = productService.existsById(1L);

        assertTrue(exists);
        verify(productRepository, times(1)).existsById(1L);
    }
}

