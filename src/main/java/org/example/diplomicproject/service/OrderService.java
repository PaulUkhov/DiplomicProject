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

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final PaymentService paymentService;
    private final ReservationService reservationService;
    private final OrderRepository orderRepository;

    // Получить все заказы
    public List<Order> findAll() {
        log.info("Получение списка всех заказов");
        return orderRepository.findAll();
    }

    // Найти заказ по ID
    public Optional<Order> findById(Long id) {
        log.info("Поиск заказа по ID: {}", id);
        Optional<Order> order = orderRepository.findById(id);
        if (order.isPresent()) {
            log.debug("Заказ найден: {}", order.get());
        } else {
            log.warn("Заказ с ID {} не найден", id);
        }
        return order;
    }

    // Сохранить или обновить заказ
    public Order save(Order order) {
        boolean isNew = order.getId() == null;
        Order savedOrder = orderRepository.save(order);
        log.info("{} заказ: ID = {}", isNew ? "Создан" : "Обновлён", savedOrder.getId());
        return savedOrder;
    }

    // Удалить заказ по ID
    public void deleteById(Long id) {
        log.info("Удаление заказа с ID: {}", id);
        orderRepository.deleteById(id);
        log.debug("Заказ с ID {} удалён", id);
    }

    // Проверка, существует ли заказ
    public boolean existsById(Long id) {
        boolean exists = orderRepository.existsById(id);
        log.info("Проверка существования заказа с ID {}: {}", id, exists);
        return exists;
    }

    // Обновить статус заказа
    @Transactional
    public void updateStatus(Long orderId, OrderStatus status) {
        log.info("Обновление статуса заказа ID {} на {}", orderId, status);

        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            order.setStatus(status);
            orderRepository.save(order);
            log.debug("Статус заказа ID {} обновлён на {}", orderId, status);
        } else {
            log.error("Не удалось обновить статус. Заказ с ID {} не найден", orderId);
            throw new RuntimeException("Order not found with ID: " + orderId);
        }
    }

    // Обработка заказа
    public boolean processOrder(OrderRequest orderRequest) {
        log.info("Начало обработки заказа: userId={}, productId={}, qty={}, total={}",
                orderRequest.getUserId(),
                orderRequest.getProductId(),
                orderRequest.getQuantity(),
                orderRequest.getTotalAmount());

        boolean reserved = reservationService.reserveProduct(orderRequest.getProductId(), orderRequest.getQuantity());
        if (!reserved) {
            log.warn("Не удалось зарезервировать товар. productId={}, quantity={}",
                    orderRequest.getProductId(), orderRequest.getQuantity());
            throw new RuntimeException("Не удалось зарезервировать товар.");
        }

        boolean paid = paymentService.processPayment(orderRequest.getUserId(), orderRequest.getTotalAmount());
        if (!paid) {
            log.warn("Ошибка при обработке платежа. userId={}, amount={}",
                    orderRequest.getUserId(), orderRequest.getTotalAmount());
            throw new RuntimeException("Ошибка оплаты. Операция отменена.");
        }

        log.info("Заказ успешно обработан для пользователя ID {}", orderRequest.getUserId());
        return true;
    }
}
