package it.adesso.management.ordermanagementservice.entities.orders;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@Embeddable
public class OrderRemovedIngredientId implements Serializable {
    private static final long serialVersionUID = 5042091656078395100L;
    @NotNull
    @Column(name = "ingredient", nullable = false)
    private Long ingredient;

    @NotNull
    @Column(name = "\"order\"", nullable = false)
    private Long order;


}