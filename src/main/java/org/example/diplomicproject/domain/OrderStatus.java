package org.example.diplomicproject.domain;

public enum OrderStatus {
    NEW,        // Новый заказ
    PAID,       // Оплачен
    SHIPPED,    // Отправлен
    DELIVERED,  // Доставлен
    CANCELLED,   // Отменён
    PROCESSING, COMPLETED
}
