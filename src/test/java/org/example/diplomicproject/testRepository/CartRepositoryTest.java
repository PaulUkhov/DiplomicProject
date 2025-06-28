package org.example.diplomicproject.testRepository;

import org.example.diplomicproject.domain.Cart;
import org.example.diplomicproject.domain.User;
import org.example.diplomicproject.repository.CartRepository;
import org.example.diplomicproject.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class CartRepositoryTest {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void testFindByUserId() {
        // Создаем и сохраняем пользователя
        User user = new User();
        user.setLogin("testuser");
        user.setEmail("testuser@example.com");
        user = userRepository.save(user);

        // Создаем корзину, связываем с пользователем и сохраняем
        Cart cart = new Cart();
        cart.setUser(user);
        cart.setTotalPrice(BigDecimal.ZERO);
        cart = cartRepository.save(cart);

        // Проверяем, что корзина найдена по userId
        Optional<Cart> foundCart = cartRepository.findByUserId(user.getId());
        assertThat(foundCart).isPresent();
        assertThat(foundCart.get().getUser().getId()).isEqualTo(user.getId());
    }
}
