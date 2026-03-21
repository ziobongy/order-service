package it.adesso.management.ordermanagementservice.services.impl;

import it.adesso.management.ordermanagementservice.DTOs.OrderDTO;
import it.adesso.management.ordermanagementservice.DTOs.SendingEventDTO;
import it.adesso.management.ordermanagementservice.enums.OrderSendingEventEnum;
import it.adesso.management.ordermanagementservice.services.OrderService;
import it.adesso.management.ordermanagementservice.services.QueueService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {

    private final QueueService queueService;

    @Override
    public void createOrder(Long extenalId, OrderDTO orderDTO) {
        this.queueService.sendMessage(
            "orderOrchestrationService-out-0",
            SendingEventDTO.builder()
                .orderId(extenalId)
                .event(OrderSendingEventEnum.CREATED).build()
        );
    }
}
