package it.adesso.management.ordermanagementservice.repositories;

import it.adesso.management.ordermanagementservice.entities.orders.OrderEntryAddedIngredient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderEntryAddedIngredientsRepository extends JpaRepository<OrderEntryAddedIngredient, Long> {
}
