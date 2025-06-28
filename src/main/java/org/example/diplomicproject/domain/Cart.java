package org.example.diplomicproject.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;


@Builder
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "carts")
@Schema(description = "Корзина пользователя, содержащая список товаров и общую сумму")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Идентификатор корзины", example = "1")
    private Long id;

    @Column(name = "total_price")
    @Schema(description = "Общая сумма товаров в корзине", example = "299.99")
    private BigDecimal totalPrice;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @Schema(description = "Пользователь, владеющий корзиной")
    private User user;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    @Schema(description = "Список товаров в корзине")
    private List<CartItem> items = new ArrayList<>();
}

