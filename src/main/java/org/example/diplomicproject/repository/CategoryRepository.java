package org.example.diplomicproject.repository;

import org.example.diplomicproject.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByName(String name);
//    @Query("SELECT DISTINCT c FROM Category c JOIN FETCH c.products")
//    List<Category> findAllWithProducts();
}
