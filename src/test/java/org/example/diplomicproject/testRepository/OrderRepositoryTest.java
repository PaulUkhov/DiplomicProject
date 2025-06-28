package org.example.diplomicproject.testRepository;

import org.example.diplomicproject.domain.OrderStatus;
import org.example.diplomicproject.domain.Order;
import org.example.diplomicproject.domain.User;
import org.example.diplomicproject.repository.OrderRepository;
import org.example.diplomicproject.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class OrderRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    private User createTestUser() {
        User user = new User();
        user.setLogin("testuser");
        user.setEmail("testuser@example.com");
        user.setPassword("123456");
        return userRepository.save(user);
    }

    @Test
    void testSaveAndFindById() {
        User user = createTestUser();

        Order order = new Order();
        order.setStatus(OrderStatus.NEW);
        order.setCreatedDate(LocalDateTime.now());
        order.setLastModifiedDate(LocalDateTime.now());
        order.setUser(user);

        order = orderRepository.save(order);

        Optional<Order> found = orderRepository.findById(order.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getId()).isEqualTo(order.getId());
    }

    @Test
    void testDeleteById() {
        User user = createTestUser();

        Order order = new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.NEW);
        order.setCreatedDate(LocalDateTime.now());
        order.setLastModifiedDate(LocalDateTime.now());
        order = orderRepository.save(order);

        Long id = order.getId();
        orderRepository.deleteById(id);

        Optional<Order> deleted = orderRepository.findById(id);
        assertThat(deleted).isEmpty();
    }

    @Test
    void testFindAll() {
        User user = createTestUser();

        Order order1 = new Order();
        order1.setUser(user);
        order1.setStatus(OrderStatus.NEW);
        order1.setCreatedDate(LocalDateTime.now());
        order1.setLastModifiedDate(LocalDateTime.now());

        Order order2 = new Order();
        order2.setUser(user);
        order2.setStatus(OrderStatus.NEW);
        order2.setCreatedDate(LocalDateTime.now());
        order2.setLastModifiedDate(LocalDateTime.now());

        orderRepository.save(order1);
        orderRepository.save(order2);

        List<Order> allOrders = orderRepository.findAll();
        System.out.println("Orders in DB: " + allOrders.size()); // ← проверка

        assertThat(allOrders).hasSize(2);
    }

}
