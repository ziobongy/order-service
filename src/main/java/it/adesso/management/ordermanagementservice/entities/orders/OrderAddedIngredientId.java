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
public class OrderAddedIngredientId implements Serializable {
    private static final long serialVersionUID = 7929992032919597808L;
    @NotNull
    @Column(name = "\"order\"", nullable = false)
    private Long order;

    @NotNull
    @Column(name = "ingredient", nullable = false)
    private Long ingredient;


}