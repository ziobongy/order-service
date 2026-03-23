package it.adesso.management.ordermanagementservice.repositories.external;

import it.adesso.management.ordermanagementservice.entities.external.PizzaIngredient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PizzaIngredientsRepository extends JpaRepository<PizzaIngredient, Long> {
}
