package it.adesso.management.ordermanagementservice.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderStatusEnum {
    DELETED("DELETED"),
    COMPLETED("COMPLETED"),
    CHARGED("CHARGED"),
    PAYED("PAYED"),
    UNDER_PAYMENT("UNDER_PAYMENT"),
    ACQUIRED("ACQUIRED"),
    UNDER_EDITING("UNDER_EDITING"),
    CREATED("CREATED");

    private final String name;
}
