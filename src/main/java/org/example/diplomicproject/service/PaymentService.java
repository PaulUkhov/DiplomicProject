package org.example.diplomicproject.service;

import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    public boolean processPayment(Long userId, double amount) {
        // Вызов внешнего сервиса оплаты (или имитация)
        System.out.println("Processing payment for user " + userId + " amount: " + amount);

        // Имитация успешной оплаты
        return true;
    }
}