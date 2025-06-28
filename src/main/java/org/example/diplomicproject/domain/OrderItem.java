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
@Table(name = "order_items")
@Schema(description = "Позиция заказа (товар в заказе)")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Уникальный идентификатор позиции", example = "1")
    private Long id;

    @Column(name = "quantity")
    @Schema(description = "Количество товара", example = "2")
    private Integer quantity;

    @Column(name = "price_at_purchase", precision = 19, scale = 2)
    @Schema(description = "Цена товара на момент покупки", example = "1499.99")
    private BigDecimal priceAtPurchase;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    @Schema(description = "Связанный заказ")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id")
    @Schema(description = "Товар, связанный с позицией")
    private Product product;
}
