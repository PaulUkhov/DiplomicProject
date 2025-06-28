package org.example.diplomicproject.service;

import lombok.RequiredArgsConstructor;
import org.example.diplomicproject.domain.Category;
import org.example.diplomicproject.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    // Получить все категории
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    // Найти категорию по ID
    public Optional<Category> findById(Long id) {
        return categoryRepository.findById(id);
    }

    // Создать или обновить категорию
    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    // Удалить категорию по ID
    public void deleteById(Long id) {
        categoryRepository.deleteById(id);
    }

    // Проверка существования категории по имени
    public Optional<Category> existsByName(String name) {
        return categoryRepository.findByName(name);
    }
}
