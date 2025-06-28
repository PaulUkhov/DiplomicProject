package org.example.diplomicproject.service;

import org.springframework.stereotype.Service;

@Service
public class ReservationService {

    public boolean reserveProduct(Long productId, int quantity) {
        // Здесь мог бы быть вызов складского микросервиса
        System.out.println("Reserving product " + productId + " quantity: " + quantity);

        // Имитация успешного резервирования
        return true;
    }
}
