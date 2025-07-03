package org.example.diplomicproject.service;

import lombok.RequiredArgsConstructor;
import org.example.diplomicproject.domain.Category;
import org.example.diplomicproject.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    // Получить все категории
    public List<Category> findAll() {
        log.info("Получение всех категорий");
        return categoryRepository.findAll();
    }

    // Найти категорию по ID
    public Optional<Category> findById(Long id) {
        log.info("Поиск категории по ID: {}", id);
        Optional<Category> category = categoryRepository.findById(id);
        if (category.isPresent()) {
            log.debug("Категория найдена: {}", category.get().getName());
        } else {
            log.warn("Категория с ID {} не найдена", id);
        }
        return category;
    }

    // Создать или обновить категорию
    public Category save(Category category) {
        boolean isNew = category.getId() == null;
        Category saved = categoryRepository.save(category);
        log.info("{} категория: ID = {}, Name = {}",
                isNew ? "Создана новая" : "Обновлена",
                saved.getId(), saved.getName());
        return saved;
    }

    // Удалить категорию по ID
    public void deleteById(Long id) {
        log.info("Удаление категории с ID: {}", id);
        categoryRepository.deleteById(id);
        log.debug("Категория с ID {} удалена", id);
    }

    // Проверка существования категории по имени
    public Optional<Category> existsByName(String name) {
        log.info("Проверка существования категории по имени: {}", name);
        Optional<Category> result = categoryRepository.findByName(name);
        if (result.isPresent()) {
            log.debug("Категория с именем '{}' найдена (ID = {})", name, result.get().getId());
        } else {
            log.debug("Категория с именем '{}' не найдена", name);
        }
        return result;
    }
}
