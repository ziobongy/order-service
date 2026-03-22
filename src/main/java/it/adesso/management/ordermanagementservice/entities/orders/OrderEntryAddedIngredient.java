package it.adesso.management.ordermanagementservice.entities.orders;

import it.adesso.management.ordermanagementservice.entities.external.Ingredient;
import jakarta.persistence.*;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "order_added_ingredients")
public class OrderEntryAddedIngredient {
    @EmbeddedId
    private OrderEntryAddedIngredientId id;

    @MapsId("orderEntry")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_entry", nullable = false)
    private OrderEntry orderEntry;

    @MapsId("ingredient")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ingredient", nullable = false)
    private Ingredient ingredient;


}