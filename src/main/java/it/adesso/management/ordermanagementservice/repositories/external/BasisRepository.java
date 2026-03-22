package it.adesso.management.ordermanagementservice.repositories.external;

import it.adesso.management.ordermanagementservice.entities.external.Basis;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BasisRepository extends JpaRepository<Basis, Long> {
}
