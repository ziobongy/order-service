package it.adesso.management.ordermanagementservice.entities.orders;

import it.adesso.management.ordermanagementservice.entities.external.Basis;
import it.adesso.management.ordermanagementservice.entities.external.ComposedPizza;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_pizza")
    private ComposedPizza idPizza;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "base")
    private Basis base;


}