package it.adesso.management.ordermanagementservice.repositories;

import it.adesso.management.ordermanagementservice.entities.orders.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByExternalId(Long externalId);
    Page<Order> findAllByStatus(String status, Pageable pageable);
}
