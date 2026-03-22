package it.adesso.management.ordermanagementservice.DTOs.BFF;

import it.adesso.management.ordermanagementservice.DTOs.OrderEntryDTO;
import it.adesso.management.ordermanagementservice.DTOs.external.anagservice.BaseDTO;
import it.adesso.management.ordermanagementservice.DTOs.external.anagservice.IngredientDTO;
import it.adesso.management.ordermanagementservice.DTOs.external.anagservice.PizzaDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
public class OrderBffDTO {
    private Long id;
    private String status;
    private List<OrderEntryBffDTO> entries;
}
