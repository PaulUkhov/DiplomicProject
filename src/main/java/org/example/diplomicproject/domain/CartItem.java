package org.example.diplomicproject.domain;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;

@Builder
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "cart_items")
public class CartItem {

    @jakarta.persistence.Id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Количество выбранного товара
    @Column(name = "quantity")
    private Integer quantity;

    // Цена на момент добавления в корзину
    @Column(name = "price_at_add",precision = 19,scale = 2)
    private BigDecimal priceAtAdd;

    // Связь с корзиной
    @ManyToOne
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    // Связь с продуктом
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;


}
