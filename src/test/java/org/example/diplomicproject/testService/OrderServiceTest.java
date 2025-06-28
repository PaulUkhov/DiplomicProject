package org.example.diplomicproject.testService;

import org.example.diplomicproject.domain.OrderStatus;
import org.example.diplomicproject.domain.Order;
import org.example.diplomicproject.repository.OrderRepository;
import org.example.diplomicproject.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    private Order order;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        order = new Order();
        order.setId(1L);
        order.setStatus(OrderStatus.NEW); // Предположим, у тебя есть enum OrderStatus
    }

    @Test
    void testFindAll() {
        List<Order> orders = List.of(order);
        when(orderRepository.findAll()).thenReturn(orders);

        List<Order> result = orderService.findAll();

        assertEquals(1, result.size());
        assertEquals(OrderStatus.NEW, result.get(0).getStatus());
        verify(orderRepository, times(1)).findAll();
    }

    @Test
    void testFindById() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        Optional<Order> result = orderService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(OrderStatus.NEW, result.get().getStatus());
        verify(orderRepository, times(1)).findById(1L);
    }

    @Test
    void testSave() {
        when(orderRepository.save(order)).thenReturn(order);

        Order saved = orderService.save(order);

        assertNotNull(saved);
        assertEquals(OrderStatus.NEW, saved.getStatus());
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    void testDeleteById() {
        doNothing().when(orderRepository).deleteById(1L);

        orderService.deleteById(1L);

        verify(orderRepository, times(1)).deleteById(1L);
    }

    @Test
    void testExistsById() {
        when(orderRepository.existsById(1L)).thenReturn(true);

        boolean exists = orderService.existsById(1L);

        assertTrue(exists);
        verify(orderRepository, times(1)).existsById(1L);
    }

    @Test
    void testUpdateStatus_WhenOrderExists() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        orderService.updateStatus(1L, OrderStatus.COMPLETED);

        assertEquals(OrderStatus.COMPLETED, order.getStatus());
        verify(orderRepository, times(1)).findById(1L);
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    void testUpdateStatus_WhenOrderNotFound() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            orderService.updateStatus(1L, OrderStatus.COMPLETED);
        });

        assertEquals("Order not found with ID: 1", exception.getMessage());
        verify(orderRepository, times(1)).findById(1L);
        verify(orderRepository, never()).save(any());
    }
}
