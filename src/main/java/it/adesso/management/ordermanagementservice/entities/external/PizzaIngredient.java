package it.adesso.management.ordermanagementservice.entities.external;

import jakarta.persistence.*;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "pizza_ingredients", schema = "external")
public class PizzaIngredient {
    @EmbeddedId
    private PizzaIngredientId id;

    @MapsId("ingredient")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ingredient", nullable = false)
    private Ingredient ingredient;

    @MapsId("pizza")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "pizza", nullable = false)
    private ComposedPizza pizza;


}