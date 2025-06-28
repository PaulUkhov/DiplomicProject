package org.example.diplomicproject.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
@Builder
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "carts")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Сумма корзины (можно рассчитывать динамически)
    @Column(name = "total_price")
    private BigDecimal totalPrice;

    // Пользователь, владеющий корзиной
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // Список товаров в корзине
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> items = new ArrayList<>();


}
