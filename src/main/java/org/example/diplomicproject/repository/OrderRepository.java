package org.example.diplomicproject.repository;

import org.example.diplomicproject.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(User user);
    Optional<Order> findByStatus(OrderStatus status);

}
