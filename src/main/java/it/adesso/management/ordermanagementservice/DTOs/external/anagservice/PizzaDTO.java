package it.adesso.management.ordermanagementservice.DTOs.external.anagservice;

import lombok.*;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PizzaDTO {
    private Long id;
    private String name;
    private BaseDTO base;
    private List<IngredientDTO> ingredients;
}
