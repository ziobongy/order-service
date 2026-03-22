package it.adesso.management.ordermanagementservice.DTOs.external.anagservice;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PizzaDTO {
    private Long id;
    private String name;
    private BaseDTO baseDTO;
}
