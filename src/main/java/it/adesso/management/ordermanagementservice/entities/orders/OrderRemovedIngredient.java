package it.adesso.management.ordermanagementservice.entities.orders;

import it.adesso.management.ordermanagementservice.entities.external.Ingredient;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "order_removed_ingredients")
public class OrderRemovedIngredient {
    @EmbeddedId
    private OrderRemovedIngredientId id;

    @MapsId("ingredient")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ingredient", nullable = false)
    private Ingredient ingredient;

    @MapsId("order")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "\"order\"", nullable = false)
    private Order order;


}