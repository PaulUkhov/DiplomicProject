package org.example.diplomicproject.service;

import lombok.RequiredArgsConstructor;
import org.example.diplomicproject.domain.OrderStatus;
import org.example.diplomicproject.domain.Order;
import org.example.diplomicproject.dto.OrderRequest;
import org.example.diplomicproject.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final PaymentService paymentService;
    private final ReservationService reservationService;
    private final OrderRepository orderRepository;

    // Получить все заказы
    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    // Найти заказ по ID
    public Optional<Order> findById(Long id) {
        return orderRepository.findById(id);
    }

    // Сохранить или обновить заказ
    public Order save(Order order) {
        return orderRepository.save(order);
    }

    // Удалить заказ по ID
    public void deleteById(Long id) {
        orderRepository.deleteById(id);
    }

    // Проверка, существует ли заказ
    public boolean existsById(Long id) {
        return orderRepository.existsById(id);
    }

    // Обновить статус заказа
    @Transactional
    public void updateStatus(Long orderId, OrderStatus status) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            order.setStatus(status);
            orderRepository.save(order);
        } else {
            throw new RuntimeException("Order not found with ID: " + orderId);
        }
    }
    public boolean processOrder(OrderRequest orderRequest) {
        boolean reserved = reservationService.reserveProduct(orderRequest.getProductId(), orderRequest.getQuantity());

        if (!reserved) {
            throw new RuntimeException("Не удалось зарезервировать товар.");
        }

        boolean paid = paymentService.processPayment(orderRequest.getUserId(), orderRequest.getTotalAmount());

        if (!paid) {
            throw new RuntimeException("Ошибка оплаты. Операция отменена.");
        }

        return true;
    }
}

