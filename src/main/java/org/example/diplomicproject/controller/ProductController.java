package org.example.diplomicproject.controller;

import lombok.RequiredArgsConstructor;
import org.example.diplomicproject.domain.Product;
import org.example.diplomicproject.service.ProductService;
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
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Tag(name = "Продукты", description = "Операции с продуктами")
public class ProductController {

    private final ProductService productService;

    @Operation(summary = "Получить все продукты", description = "Возвращает список всех продуктов")
    @ApiResponse(responseCode = "200", description = "Успешно",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Product.class)))
    @GetMapping
    public List<Product> getAllProducts() {
        return productService.findAll();
    }

    @Operation(summary = "Получить продукт по ID", description = "Возвращает продукт по идентификатору")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Продукт найден"),
            @ApiResponse(responseCode = "404", description = "Продукт не найден")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(
            @Parameter(description = "ID продукта") @PathVariable Long id) {
        return productService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Создать новый продукт", description = "Создает новый продукт")
    @ApiResponse(responseCode = "201", description = "Продукт создан")
    @PostMapping
    public ResponseEntity<Product> createProduct(
            @Parameter(description = "Данные нового продукта") @RequestBody Product product) {
        Product savedProduct = productService.save(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);
    }

    @Operation(summary = "Обновить продукт", description = "Обновляет продукт по ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Продукт обновлён"),
            @ApiResponse(responseCode = "404", description = "Продукт не найден")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(
            @Parameter(description = "ID продукта") @PathVariable Long id,
            @Parameter(description = "Обновлённые данные продукта") @RequestBody Product product) {
        if (!productService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        product.setId(id);
        Product updatedProduct = productService.save(product);
        return ResponseEntity.ok(updatedProduct);
    }

    @Operation(summary = "Удалить продукт", description = "Удаляет продукт по ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Продукт удалён"),
            @ApiResponse(responseCode = "404", description = "Продукт не найден")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(
            @Parameter(description = "ID продукта") @PathVariable Long id) {
        if (!productService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        productService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}


