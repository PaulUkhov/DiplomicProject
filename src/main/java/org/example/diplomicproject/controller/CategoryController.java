package org.example.diplomicproject.controller;

import lombok.RequiredArgsConstructor;
import org.example.diplomicproject.domain.Category;
import org.example.diplomicproject.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    // Получить все категории
    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories() {
        return ResponseEntity.ok(categoryService.findAll());
    }

    // Получить категорию по ID
    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable Long id) {
        return categoryService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Создать новую категорию
    @PostMapping
    public ResponseEntity<?> createCategory(@RequestBody Category category) {
        if (categoryService.existsByName(category.getName()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Категория с таким именем уже существует");
        }

        Category saved = categoryService.save(category);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // Обновить категорию
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable Long id, @RequestBody Category category) {
        return categoryService.findById(id)
                .map(existing -> {
                    existing.setName(category.getName());
                    Category updated = categoryService.save(existing);
                    return ResponseEntity.ok(updated);
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // Удалить категорию
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id) {
        if (categoryService.findById(id).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Категория не найдена");
        }

        categoryService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
