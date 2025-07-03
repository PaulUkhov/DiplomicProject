package org.example.diplomicproject.service;

import lombok.RequiredArgsConstructor;
import org.example.diplomicproject.domain.Product;
import org.example.diplomicproject.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    // Получить все продукты
    public List<Product> findAll() {
        log.info("Получение всех продуктов");
        List<Product> products = productRepository.findAll();
        log.debug("Найдено {} продуктов", products.size());
        return products;
    }

    // Найти продукт по ID
    public Optional<Product> findById(Long id) {
        log.info("Поиск продукта по ID: {}", id);
        Optional<Product> product = productRepository.findById(id);
        if (product.isPresent()) {
            log.debug("Продукт найден: {}", product.get().getName());
        } else {
            log.warn("Продукт с ID {} не найден", id);
        }
        return product;
    }

    // Создать или обновить продукт
    public Product save(Product product) {
        boolean isNew = product.getId() == null;
        Product saved = productRepository.save(product);
        log.info("{} продукт: ID = {}, Name = {}",
                isNew ? "Создан" : "Обновлён",
                saved.getId(), saved.getName());
        return saved;
    }

    // Удалить продукт по ID
    public void deleteById(Long id) {
        log.info("Удаление продукта с ID: {}", id);
        productRepository.deleteById(id);
        log.debug("Продукт с ID {} удалён", id);
    }

    // Проверка, существует ли продукт по ID
    public boolean existsById(Long id) {
        boolean exists = productRepository.existsById(id);
        log.info("Проверка существования продукта с ID {}: {}", id, exists);
        return exists;
    }

    // Заготовка для будущей фильтрации
    // public List<Product> findProductsByFilter(FilterCriteria criteria) {
    //     log.info("Фильтрация продуктов по критериям: {}", criteria);
    //     ...
    // }
}


