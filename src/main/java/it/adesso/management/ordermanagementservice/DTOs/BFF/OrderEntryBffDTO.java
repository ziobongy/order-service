package it.adesso.management.ordermanagementservice.DTOs.BFF;

import it.adesso.management.ordermanagementservice.DTOs.external.anagservice.BaseDTO;
import it.adesso.management.ordermanagementservice.DTOs.external.anagservice.IngredientDTO;
import it.adesso.management.ordermanagementservice.DTOs.external.anagservice.PizzaDTO;
import lombok.*;

import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderEntryBffDTO {
    private Long id;
    private Long orderId;
    private PizzaDTO pizza;
    private BaseDTO base;
    private Set<IngredientDTO> addedIngredients;
    private Set<IngredientDTO> removedIngredients;
}
