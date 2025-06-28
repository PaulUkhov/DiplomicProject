package org.example.diplomicproject.testRepository;


import org.example.diplomicproject.domain.Cart;
import org.example.diplomicproject.domain.CartItem;
import org.example.diplomicproject.domain.Product;
import org.example.diplomicproject.domain.User;
import org.example.diplomicproject.repository.CartItemRepository;
import org.example.diplomicproject.repository.CartRepository;
import org.example.diplomicproject.repository.ProductRepository;
import org.example.diplomicproject.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;


import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class CartItemRepositoryTest {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    void testSaveAndFind() {
        User user = userRepository.save(User.builder()
                .login("Pasha")
                .email("pasha@gmail.com")
                .build());
        // Создаём и сохраняем необходимые сущности (Cart и Product) для связи с CartItem
        Product product = new Product();
        product.setName("Test Product");
        product.setPrice(new BigDecimal("10.00"));
        product = productRepository.save(product);

        Cart cart = new Cart();
        cart.setTotalPrice(BigDecimal.ZERO);
        cart.setUser(user);
        cart = cartRepository.save(cart);

        // Создаём CartItem и сохраняем
        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setProduct(product);
        cartItem.setQuantity(2);
        cartItem.setPriceAtAdd(product.getPrice());

        cartItem = cartItemRepository.save(cartItem);

        // Проверяем, что сохранилось и можем найти
        List<CartItem> items = cartItemRepository.findAll();
        assertThat(items).isNotEmpty();
        assertThat(items.get(0).getProduct().getName()).isEqualTo("Test Product");
    }
}
