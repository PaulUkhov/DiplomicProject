package org.example.diplomicproject.domain;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "products")
@Schema(description = "Сущность товара")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Уникальный идентификатор продукта", example = "1")
    private Long id;

    @Column(name = "name", nullable = false)
    @Schema(description = "Название продукта", example = "Смартфон Samsung Galaxy S23")
    private String name;

    @Column(name = "description")
    @Schema(description = "Описание продукта", example = "Флагманский смартфон с отличной камерой")
    private String description;

    @Column(name = "price", nullable = false, precision = 19, scale = 2)
    @Schema(description = "Цена продукта", example = "129999.99")
    private BigDecimal price;

    @Column(name = "average_rating")
    @Schema(description = "Средняя оценка продукта", example = "4.5")
    private Double averageRating;

    @ManyToOne
    @JoinColumn(name = "category_id")
    @Schema(description = "Категория продукта")
    private Category category;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    @Schema(description = "Отзывы о продукте")
    private List<Review> reviews;
}

