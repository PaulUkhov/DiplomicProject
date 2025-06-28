package org.example.diplomicproject.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "categories")
@Schema(description = "Категория товара")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Уникальный идентификатор категории", example = "1")
    private Long id;

    @Column(name = "name", nullable = false, length = 50, unique = true)
    @Schema(description = "Название категории", example = "Электроника")
    private String name;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    @Schema(description = "Список продуктов, относящихся к категории")
    private List<Product> products;
}
