package org.example.diplomicproject.testRepository;

import org.example.diplomicproject.domain.Category;
import org.example.diplomicproject.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void testSaveAndExistsByName() {
        // Создаём и сохраняем категорию
        Category category = new Category();
        category.setName("Electronics");
        category = categoryRepository.save(category);

        // Проверяем, что категория сохранилась
        Optional<Category> found = categoryRepository.findById(category.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Electronics");

        // Проверяем метод findByName для существующей категории
        Optional<Category> exists = categoryRepository.findByName("Electronics");
        assertThat(exists).isPresent();

        // Проверяем, что несуществующая категория не найдена
        Optional<Category> notExists = categoryRepository.findByName("Toys");
        assertThat(notExists).isEmpty();
    }


    @Test
    void testDeleteById() {
        Category category = new Category();
        category.setName("Books");
        category = categoryRepository.save(category);

        Long id = category.getId();
        categoryRepository.deleteById(id);

        Optional<Category> deleted = categoryRepository.findById(id);
        assertThat(deleted).isEmpty();
    }
}
