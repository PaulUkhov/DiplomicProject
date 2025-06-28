package org.example.diplomicproject.controller;

import lombok.RequiredArgsConstructor;
import org.example.diplomicproject.domain.Category;
import org.example.diplomicproject.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@Tag(name = "Категории", description = "Управление категориями продуктов")
public class CategoryController {

    private final CategoryService categoryService;

    @Operation(summary = "Получить все категории", description = "Возвращает список всех категорий")
    @ApiResponse(responseCode = "200", description = "Успешно получен список категорий")
    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories() {
        return ResponseEntity.ok(categoryService.findAll());
    }

    @Operation(summary = "Получить категорию по ID", description = "Возвращает категорию по заданному ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Категория найдена"),
            @ApiResponse(responseCode = "404", description = "Категория не найдена")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(
            @Parameter(description = "ID категории") @PathVariable Long id) {
        return categoryService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Создать новую категорию", description = "Создаёт новую категорию, если имя уникально")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Категория успешно создана"),
            @ApiResponse(responseCode = "409", description = "Категория с таким именем уже существует")
    })
    @PostMapping
    public ResponseEntity<?> createCategory(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Новая категория",
                    required = true,
                    content = @Content(schema = @Schema(implementation = Category.class))
            )
            @RequestBody Category category) {
        if (categoryService.existsByName(category.getName()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Категория с таким именем уже существует");
        }

        Category saved = categoryService.save(category);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @Operation(summary = "Обновить категорию", description = "Обновляет имя категории по её ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Категория обновлена"),
            @ApiResponse(responseCode = "404", description = "Категория не найдена")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategory(
            @Parameter(description = "ID категории") @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Обновлённая категория",
                    required = true,
                    content = @Content(schema = @Schema(implementation = Category.class))
            )
            @RequestBody Category category) {
        return categoryService.findById(id)
                .map(existing -> {
                    existing.setName(category.getName());
                    Category updated = categoryService.save(existing);
                    return ResponseEntity.ok(updated);
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @Operation(summary = "Удалить категорию", description = "Удаляет категорию по ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Категория удалена"),
            @ApiResponse(responseCode = "404", description = "Категория не найдена")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(
            @Parameter(description = "ID категории") @PathVariable Long id) {
        if (categoryService.findById(id).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Категория не найдена");
        }

        categoryService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

