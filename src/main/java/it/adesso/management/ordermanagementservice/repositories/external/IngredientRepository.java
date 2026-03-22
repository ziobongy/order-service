package it.adesso.management.ordermanagementservice.repositories.external;

import it.adesso.management.ordermanagementservice.entities.external.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
}
