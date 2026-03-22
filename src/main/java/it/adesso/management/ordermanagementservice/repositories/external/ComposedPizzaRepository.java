package it.adesso.management.ordermanagementservice.repositories.external;

import it.adesso.management.ordermanagementservice.entities.external.ComposedPizza;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ComposedPizzaRepository extends JpaRepository<ComposedPizza, Long> {
}
