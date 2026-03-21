package it.adesso.management.ordermanagementservice.DTOs;

import it.adesso.management.ordermanagementservice.enums.ActionEventEnum;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class ReceivingEventDTO {
    private Long orderId;
    private ActionEventEnum event;
    private OrderDTO order;
    private String message;
}
