package it.adesso.management.ordermanagementservice.entities.orders;

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
public class OrderEntryAddedIngredientId implements Serializable {
    private static final long serialVersionUID = -8493145296708052628L;
    @NotNull
    @Column(name = "order_entry", nullable = false)
    private Long orderEntry;

    @NotNull
    @Column(name = "ingredient", nullable = false)
    private Long ingredient;


}