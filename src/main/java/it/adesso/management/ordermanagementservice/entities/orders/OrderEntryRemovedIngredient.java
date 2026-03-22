package it.adesso.management.ordermanagementservice.entities.orders;

import it.adesso.management.ordermanagementservice.entities.external.Ingredient;
import jakarta.persistence.*;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "order_removed_ingredients")
public class OrderEntryRemovedIngredient {
    @EmbeddedId
    private OrderEntryRemovedIngredientId id;

    @MapsId("ingredient")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ingredient", nullable = false)
    private Ingredient ingredient;

    @MapsId("orderEntry")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_entry", nullable = false)
    private OrderEntry orderEntry;


}