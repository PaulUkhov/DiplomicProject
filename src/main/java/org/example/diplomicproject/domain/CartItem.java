package org.example.diplomicproject.domain;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import io.swagger.v3.oas.annotations.media.Schema;


@Builder
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "cart_items")
@Schema(description = "Элемент корзины — содержит товар, его количество и цену на момент добавления")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Идентификатор элемента корзины", example = "1")
    private Long id;

    @Column(name = "quantity")
    @Schema(description = "Количество выбранного товара", example = "2")
    private Integer quantity;

    @Column(name = "price_at_add", precision = 19, scale = 2)
    @Schema(description = "Цена на момент добавления в корзину", example = "49.99")
    private BigDecimal priceAtAdd;

    @ManyToOne
    @JoinColumn(name = "cart_id", nullable = false)
    @Schema(description = "Корзина, к которой относится этот элемент")
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "product_id")
    @Schema(description = "Продукт, связанный с этим элементом корзины")
    private Product product;
}

