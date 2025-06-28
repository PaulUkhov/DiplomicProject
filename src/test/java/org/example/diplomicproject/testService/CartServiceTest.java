package org.example.diplomicproject.testService;

import org.example.diplomicproject.domain.Cart;
import org.example.diplomicproject.domain.CartItem;
import org.example.diplomicproject.domain.Product;
import org.example.diplomicproject.domain.User;
import org.example.diplomicproject.repository.CartItemRepository;
import org.example.diplomicproject.repository.CartRepository;
import org.example.diplomicproject.repository.ProductRepository;
import org.example.diplomicproject.repository.UserRepository;
import org.example.diplomicproject.service.CartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CartServiceTest {

    @Mock
    private CartRepository cartRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private CartItemRepository cartItemRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CartService cartService;

    private User user;
    private Cart cart;
    private Product product;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1L);

        cart = new Cart();
        cart.setUser(user);
        cart.setItems(new ArrayList<>());
        cart.setTotalPrice(BigDecimal.ZERO);

        product = new Product();
        product.setId(100L);
        product.setPrice(BigDecimal.valueOf(10));
    }

    @Test
    void testGetCartByUserId_WhenCartExists() {
        when(cartRepository.findByUserId(user.getId())).thenReturn(Optional.of(cart));

        Cart result = cartService.getCartByUserId(user.getId());

        assertEquals(cart, result);
        verify(cartRepository, times(1)).findByUserId(user.getId());
        verifyNoMoreInteractions(cartRepository);
    }

    @Test
    void testGetCartByUserId_WhenCartDoesNotExist() {
        when(cartRepository.findByUserId(user.getId())).thenReturn(Optional.empty());
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(cartRepository.save(any(Cart.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Cart result = cartService.getCartByUserId(user.getId());

        assertNotNull(result);
        assertEquals(user, result.getUser());
        assertEquals(BigDecimal.ZERO, result.getTotalPrice());
        verify(cartRepository).findByUserId(user.getId());
        verify(userRepository).findById(user.getId());
        verify(cartRepository).save(any(Cart.class));
    }

    @Test
    void testAddProductToCart_NewItem() {
        when(cartRepository.findByUserId(user.getId())).thenReturn(Optional.of(cart));
        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));
        when(cartRepository.save(any(Cart.class))).thenAnswer(invocation -> invocation.getArgument(0));

        cartService.addProductToCart(user.getId(), product.getId(), 2);

        assertEquals(1, cart.getItems().size());
        assertEquals(product, cart.getItems().get(0).getProduct());
        assertEquals(2, cart.getItems().get(0).getQuantity());
        assertEquals(product.getPrice().multiply(BigDecimal.valueOf(2)), cart.getTotalPrice());
        verify(cartRepository).save(cart);
    }

    @Test
    void testAddProductToCart_ExistingItem() {
        CartItem item = new CartItem();
        item.setProduct(product);
        item.setQuantity(1);
        item.setPriceAtAdd(product.getPrice());
        cart.getItems().add(item);

        when(cartRepository.findByUserId(user.getId())).thenReturn(Optional.of(cart));
        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));
        when(cartRepository.save(any(Cart.class))).thenAnswer(invocation -> invocation.getArgument(0));

        cartService.addProductToCart(user.getId(), product.getId(), 3);

        assertEquals(1, cart.getItems().size());
        assertEquals(4, cart.getItems().get(0).getQuantity()); // 1 + 3
        assertEquals(product.getPrice().multiply(BigDecimal.valueOf(4)), cart.getTotalPrice());
        verify(cartRepository).save(cart);
    }

    @Test
    void testRemoveProductFromCart() {
        CartItem item = new CartItem();
        item.setProduct(product);
        cart.getItems().add(item);

        when(cartRepository.findByUserId(user.getId())).thenReturn(Optional.of(cart));
        when(cartRepository.save(any(Cart.class))).thenAnswer(invocation -> invocation.getArgument(0));

        cartService.removeProductFromCart(user.getId(), product.getId());

        assertTrue(cart.getItems().isEmpty());
        assertEquals(BigDecimal.ZERO, cart.getTotalPrice());
        verify(cartRepository).save(cart);
    }

    @Test
    void testClearCart() {
        CartItem item = new CartItem();
        item.setProduct(product);
        item.setQuantity(5);
        item.setPriceAtAdd(product.getPrice());
        cart.getItems().add(item);
        cart.setTotalPrice(BigDecimal.valueOf(50));

        when(cartRepository.findByUserId(user.getId())).thenReturn(Optional.of(cart));
        when(cartRepository.save(any(Cart.class))).thenAnswer(invocation -> invocation.getArgument(0));

        cartService.clearCart(user.getId());

        assertTrue(cart.getItems().isEmpty());
        assertEquals(BigDecimal.ZERO, cart.getTotalPrice());
        verify(cartRepository).save(cart);
    }
}
