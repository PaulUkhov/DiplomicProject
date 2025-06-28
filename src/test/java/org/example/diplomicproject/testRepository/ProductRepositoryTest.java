package org.example.diplomicproject.testRepository;

import org.example.diplomicproject.domain.Category;
import org.example.diplomicproject.domain.Product;
import org.example.diplomicproject.repository.CategoryRepository;
import org.example.diplomicproject.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void testSaveAndFindById() {
        // Создаем и сохраняем категорию (если у продукта обязательное поле category)
        Category category = new Category();
        category.setName("Test Category");
        category = categoryRepository.save(category);

        // Создаем продукт
        Product product = new Product();
        product.setName("Test Product");
        product.setPrice(BigDecimal.valueOf(99.99));
        product.setDescription("Описание продукта");
        product.setCategory(category); // обязательное поле, если не nullable

        // Сохраняем продукт
        product = productRepository.save(product);

        // Проверка: извлекаем по ID
        Optional<Product> found = productRepository.findById(product.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Test Product");
    }

    @Test
    void testDeleteById() {
        Category category = new Category();
        category.setName("Category for Delete");
        category = categoryRepository.save(category);

        Product product = new Product();
        product.setName("To Delete");
        product.setDescription("Будет удален");
        product.setPrice(BigDecimal.TEN);
        product.setCategory(category);

        product = productRepository.save(product);
        Long id = product.getId();

        productRepository.deleteById(id);

        Optional<Product> deleted = productRepository.findById(id);
        assertThat(deleted).isEmpty();
    }

    @Test
    void testFindAll() {
        Category category = new Category();
        category.setName("Category for Many");
        category = categoryRepository.save(category);

        Product product1 = new Product();
        product1.setName("P1");
        product1.setDescription("Desc1");
        product1.setPrice(BigDecimal.ONE);
        product1.setCategory(category);

        Product product2 = new Product();
        product2.setName("P2");
        product2.setDescription("Desc2");
        product2.setPrice(BigDecimal.TEN);
        product2.setCategory(category);

        productRepository.save(product1);
        productRepository.save(product2);

        List<Product> products = productRepository.findAll();
        assertThat(products).isNotEqualTo(2);
    }
}
