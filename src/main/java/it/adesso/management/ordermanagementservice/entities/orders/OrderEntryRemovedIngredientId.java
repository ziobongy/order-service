package it.adesso.management.ordermanagementservice.entities.orders;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@Embeddable
public class OrderEntryRemovedIngredientId implements Serializable {
    private static final long serialVersionUID = -3643122266649049544L;
    @NotNull
    @Column(name = "ingredient", nullable = false)
    private Long ingredient;

    @NotNull
    @Column(name = "order_entry", nullable = false)
    private Long orderEntry;


}