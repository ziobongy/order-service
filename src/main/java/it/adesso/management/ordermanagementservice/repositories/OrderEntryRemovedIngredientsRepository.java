package it.adesso.management.ordermanagementservice.repositories;

import it.adesso.management.ordermanagementservice.entities.orders.OrderEntryAddedIngredient;
import it.adesso.management.ordermanagementservice.entities.orders.OrderEntryRemovedIngredient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderEntryRemovedIngredientsRepository extends JpaRepository<OrderEntryRemovedIngredient, Long> {
}
