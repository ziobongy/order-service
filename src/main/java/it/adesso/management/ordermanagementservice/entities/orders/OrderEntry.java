package it.adesso.management.ordermanagementservice.entities.orders;

import it.adesso.management.ordermanagementservice.entities.external.Basis;
import it.adesso.management.ordermanagementservice.entities.external.ComposedPizza;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "order_entries")
public class OrderEntry {
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

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "\"order\"", nullable = false)
    private Order order;

    @OneToMany(mappedBy = "orderEntry", fetch = FetchType.EAGER)
    private Set<OrderEntryAddedIngredient> addedIngredients;

    @OneToMany(mappedBy = "orderEntry", fetch = FetchType.EAGER)
    private Set<OrderEntryRemovedIngredient> removedIngredients;

}