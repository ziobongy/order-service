package it.adesso.management.ordermanagementservice.DTOs.external.anagservice;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BaseDTO {
    private Long id;
    private String name;
}
