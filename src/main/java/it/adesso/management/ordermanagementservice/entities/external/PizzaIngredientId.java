package it.adesso.management.ordermanagementservice.entities.external;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@Embeddable
public class PizzaIngredientId implements Serializable {
    private static final long serialVersionUID = -2504084688605029523L;
    @NotNull
    @Column(name = "ingredient", nullable = false)
    private Long ingredient;

    @NotNull
    @Column(name = "pizza", nullable = false)
    private Long pizza;


}