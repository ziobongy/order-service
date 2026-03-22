package it.adesso.management.ordermanagementservice.repositories;

import it.adesso.management.ordermanagementservice.entities.orders.OrderEntry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderEntryRepository extends JpaRepository<OrderEntry, Long> {
}
