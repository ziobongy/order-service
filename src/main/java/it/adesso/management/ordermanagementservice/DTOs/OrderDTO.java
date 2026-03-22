package it.adesso.management.ordermanagementservice.DTOs;

import lombok.*;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrderDTO {
    private Long id;
    private List<OrderEntryDTO> entries;
}
