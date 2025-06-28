package org.example.diplomicproject.testContainerPostgres;

import org.example.diplomicproject.domain.*;
import org.example.diplomicproject.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.math.BigDecimal;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.example.diplomicproject.domain.OrderStatus.COMPLETED;

@Transactional
@Testcontainers
@SpringBootTest
public class MyIntegrationTest {
    private Product product;
    private Role role;
    private User user;
    private Order order;
    private Review review;
    private OrderItem orderItem;
    private Category category;
    private CartItem cartItem;
    private Cart cart;

    @Container
    static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:16")
            .withDatabaseName("Database")
            .withUsername("user")
            .withPassword("password");

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private CartRepository cartRepository;

    @DynamicPropertySource
    static void registerPgProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
    }

    @BeforeEach
    void setUp() {
        user = userRepository.save(User.builder()
                .login("Pasha")
                .email("pasha@example.com")
                .password("securePassword")
                .build());

        role = roleRepository.save(Role.builder()
                .name("Admin")
                .build());

        product = productRepository.save(Product.builder()
                .name("Apple")
                .description("Seasonal red")
                .averageRating(50.25)
                .price(BigDecimal.valueOf(100.00))
                .build());

        review = reviewRepository.save(Review.builder()
                .rating(50)
                .comment("Good product")
                .product(product)
                .user(user)
                .build());

        order = orderRepository.save(Order.builder()
                .user(user)
                .status(OrderStatus.COMPLETED)
                .build());

        orderItem = orderItemRepository.save(OrderItem.builder()
                .quantity(1)
                .priceAtPurchase(BigDecimal.valueOf(25.00))
                .order(order)
                .product(product)
                .build());

        category = categoryRepository.save(Category.builder()
                .name("vegetables")
                .build());

        cart = cartRepository.save(Cart.builder()
                .totalPrice(BigDecimal.valueOf(100.00))
                .user(user)
                .build());

        cartItem = cartItemRepository.save(CartItem.builder()
                .quantity(50)
                .priceAtAdd(BigDecimal.valueOf(25.00))
                .cart(cart)
                .product(product)
                .build());


    }


    @Test
    void testSaveAndFindByUser() {
        Optional<User> found = userRepository.findByLogin("Pasha");
        Optional<User> found2 = userRepository.findByEmail("pasha@example.com");
        assertThat(found).isPresent();
        assertThat(found2).isPresent();
        assertThat(found.get().getLogin()).isEqualTo("Pasha");
        assertThat(found2.get().getEmail()).isEqualTo("pasha@example.com");
    }


    @Test
    void testSaveAndFindByRoleName() {
        Optional<Role> found = roleRepository.findByName("Admin");
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Admin");
    }


    @Test
    void testSaveAndFindByProduct() {
        Optional<Product> found = productRepository.findByName("Apple");
        Optional<Product> found2 = productRepository.findByDescription("Seasonal red");
        Optional<Product> found3 = productRepository.findByAverageRating(50.25);
        Optional<Product> found4 = productRepository.findByPrice(BigDecimal.valueOf(100.00));
        assertThat(found).isPresent();
        assertThat(found2).isPresent();
        assertThat(found3).isPresent();
        assertThat(found4).isPresent();
        assertThat(found.get().getName()).isEqualTo("Apple");
        assertThat(found2.get().getDescription()).isEqualTo("Seasonal red");
        assertThat(found3.get().getAverageRating()).isEqualTo(50.25);
        assertThat(found4.get().getPrice().compareTo(BigDecimal.valueOf(100.00))).isZero();


    }


    @Test
    void testSaveAndFindByReview() {
        List<Review> found = reviewRepository.findByProduct(product);
        List<Review> found2 = reviewRepository.findByUser(user);
        Optional<Review> found3 = reviewRepository.findByRating(50);
        Optional<Review> found4 = reviewRepository.findByComment("Good product");
        assertThat(found).isNotNull();
        assertThat(found2).isNotNull();
        assertThat(found3).isPresent();
        assertThat(found4).isPresent();
        assertThat(found.size()).isEqualTo(1);
        assertThat(found2.size()).isEqualTo(1);
        assertThat(found3.get().getRating()).isEqualTo(50);
        assertThat(found4.get().getComment()).isEqualTo("Good product");

    }


    @Test
    void testSaveAndFindByOrderAndUser() {
        Optional<Order> foundStatus = orderRepository.findByStatus(COMPLETED);
        List<Order> orders = orderRepository.findByUser(user);
        assertThat(foundStatus).isPresent();
        assertThat(orders).isNotEmpty();
        assertThat(foundStatus.get().getStatus()).isEqualTo(OrderStatus.COMPLETED);
        assertThat(orders).allMatch(order2 -> order2.getUser().equals(user));

    }


    @Test
    void testSaveAndFindByOrderItem() {
        List<OrderItem> foundAllOrderItems = orderItemRepository.findByOrder(order);
        List<OrderItem> foundAllProducts = orderItemRepository.findByProduct(product);
        List<OrderItem> foundByQuantity = orderItemRepository.findByQuantity(1);
        List<OrderItem> foundByPriceAtPurchase = orderItemRepository.findByPriceAtPurchase(BigDecimal.valueOf(25.00));
        Optional<OrderItem> foundByOrderAndProduct = orderItemRepository.findByOrderAndProduct(order, product);
        assertThat(foundAllOrderItems).isNotEmpty();
        assertThat(foundAllProducts).isNotEmpty();
        assertThat(foundByQuantity).isNotEmpty();
        assertThat(foundByPriceAtPurchase).isNotEmpty();
        assertThat(foundByOrderAndProduct).isPresent();
        assertThat(foundByOrderAndProduct.get().getPriceAtPurchase())
                .isEqualByComparingTo("25.00");
        assertThat(foundAllOrderItems.size()).isEqualTo(1);
        assertThat(foundAllProducts.size()).isEqualTo(1);
        assertThat(foundByQuantity.size()).isEqualTo(1);
        assertThat(foundByPriceAtPurchase)
                .anyMatch(item -> item.getPriceAtPurchase().compareTo(BigDecimal.valueOf(25.00)) == 0);
    }

    @Test
    void testSaveAndFindByCategory() {
        Optional<Category> foundCategory = categoryRepository.findByName("vegetables");
        assertThat(foundCategory).isPresent();
        assertThat(foundCategory.get().getName()).isEqualTo("vegetables");
    }

    @Test
    void testSaveAndFindByCart() {
        Optional<Cart> found = cartRepository.findById(cart.getId());
        Optional<Cart> found2 = cartRepository.findByTotalPrice(BigDecimal.valueOf(100.00));
        List<Cart> findAllCarts = cartRepository.findAll();
        assertThat(found.isPresent()).isTrue();
        assertThat(found2.isPresent()).isTrue();
        assertThat(found.get().getId()).isEqualTo(cart.getId());
        assertThat(found2.get().getTotalPrice().compareTo(BigDecimal.valueOf(100.00))).isZero();
        assertThat(findAllCarts.size()).isEqualTo(1);

    }

    @Test
    void testSaveAndFindByCartItem() {
        Optional<CartItem> found = cartItemRepository.findById(cartItem.getId());
        List<CartItem> found2 = cartItemRepository.findByQuantity(50);
        Optional<CartItem> found3 = cartItemRepository.findByPriceAtAdd(BigDecimal.valueOf(25.00));
        assertThat(found).isPresent();
        assertThat(found2).isNotEmpty();
        assertThat(found3).isPresent();
        assertThat(found.get().getId()).isEqualTo(cartItem.getId());
        assertThat(found2).anyMatch(it -> it.getQuantity().equals(50));
        assertThat(found3.get().getPriceAtAdd().compareTo(BigDecimal.valueOf(25.00))).isZero();
    }
}
