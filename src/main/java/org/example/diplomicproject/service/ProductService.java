package org.example.diplomicproject.service;

import lombok.RequiredArgsConstructor;
import org.example.diplomicproject.domain.Product;
import org.example.diplomicproject.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    // Получить все продукты
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    // Найти продукт по ID
    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    // Создать или обновить продукт
    public Product save(Product product) {
        return productRepository.save(product);
    }

    // Удалить продукт по ID
    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }

    // Проверка, существует ли продукт по ID
    public boolean existsById(Long id) {
        return productRepository.existsById(id);
    }

//    public List<Product> findProductsByFilter(FilterCriteria criteria) {
//        // Здесь будет логика фильтрации, например:
//        // - найти товары с ценой между minPrice и maxPrice
//        // - по категории
//        // - с рейтингом не ниже minRating
//        // Это можно сделать через JPQL, Criteria API или Specification (Spring Data JPA)
//
//    }

}


