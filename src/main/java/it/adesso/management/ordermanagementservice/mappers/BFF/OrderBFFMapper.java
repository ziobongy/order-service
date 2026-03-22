package it.adesso.management.ordermanagementservice.mappers.BFF;

import it.adesso.management.ordermanagementservice.DTOs.BFF.OrderBffDTO;
import it.adesso.management.ordermanagementservice.DTOs.BFF.OrderEntryBffDTO;
import it.adesso.management.ordermanagementservice.DTOs.OrderDTO;
import it.adesso.management.ordermanagementservice.DTOs.OrderEntryDTO;
import it.adesso.management.ordermanagementservice.DTOs.external.anagservice.BaseDTO;
import it.adesso.management.ordermanagementservice.DTOs.external.anagservice.IngredientDTO;
import it.adesso.management.ordermanagementservice.DTOs.external.anagservice.PizzaDTO;
import it.adesso.management.ordermanagementservice.entities.orders.Order;
import it.adesso.management.ordermanagementservice.entities.orders.OrderEntry;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface OrderBFFMapper {

    @Mapping(target = "id", source = "order.externalId")
    @Mapping(target = "status", source = "order.status")
    @Mapping(target = "entries", source = "order", qualifiedByName = "toOrderEntries")
    OrderBffDTO toOrderEntry(Order order);

    @Named("toOrderEntries")
    default List<OrderEntryBffDTO> toOrderEntries(Order order) {
        List<OrderEntryBffDTO> orderEntries = new ArrayList<>();
        for (OrderEntry entry : order.getEntries()) {
            OrderEntryBffDTO dto = OrderEntryBffDTO.builder()
                .id(entry.getId())
                .pizza(
                    (entry.getIdPizza() != null) ?
                        PizzaDTO.builder()
                            .id(entry.getIdPizza().getId())
                            .name(entry.getIdPizza().getName())
                            .build() :
                        null
                )
                .orderId(order.getId())
                .base(
                    (entry.getBase() != null) ?
                        BaseDTO.builder()
                            .id(entry.getBase().getId())
                            .name(entry.getBase().getName())
                            .build() :
                        null
                )
                .addedIngredients(
                    (entry.getAddedIngredients() != null) ?
                        entry.getAddedIngredients().stream().map(
                            ing -> IngredientDTO.builder().id(ing.getIngredient().getId())
                                .name(ing.getIngredient().getName()).build()
                        ).collect(Collectors.toSet()) :
                        null
                )
                .removedIngredients(
                    (entry.getRemovedIngredients() != null) ?
                        entry.getRemovedIngredients().stream().map(
                            ing -> IngredientDTO.builder().id(ing.getIngredient().getId())
                                .name(ing.getIngredient().getName()).build()
                        ).collect(Collectors.toSet()) :
                        null
                )
                .build();
            orderEntries.add(dto);
        }
        return orderEntries;
    }
}
