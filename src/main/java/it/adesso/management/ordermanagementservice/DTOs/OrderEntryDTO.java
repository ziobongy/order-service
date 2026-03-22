package it.adesso.management.ordermanagementservice.DTOs;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class OrderEntryDTO {
    private Long id;
    private Long orderId;
    private Long idPizza;
    private Long idBase;
    private Set<Long> addedIngredients;
    private Set<Long> removedIngredients;
}
