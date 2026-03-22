package it.adesso.management.ordermanagementservice.DTOs;

import it.adesso.management.ordermanagementservice.enums.OrderSendingEventEnum;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class SendingEventDTO {
    private Long orderId;
    private OrderSendingEventEnum event;
    private String message;
}
