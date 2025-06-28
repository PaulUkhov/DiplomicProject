package org.example.diplomicproject.controller;

import lombok.RequiredArgsConstructor;
import org.example.diplomicproject.domain.Product;
import org.example.diplomicproject.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    // Получить все продукты
    @GetMapping
    public List<Product> getAllProducts() {
        return productService.findAll();
    }

    // Получить продукт по ID
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        return productService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Создать новый продукт
    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        Product savedProduct = productService.save(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);
    }

    // Обновить продукт
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        if (!productService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        product.setId(id);  // обязательно указать id, чтобы обновить существующий продукт
        Product updatedProduct = productService.save(product);
        return ResponseEntity.ok(updatedProduct);
    }

    // Удалить продукт по ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        if (!productService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        productService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

