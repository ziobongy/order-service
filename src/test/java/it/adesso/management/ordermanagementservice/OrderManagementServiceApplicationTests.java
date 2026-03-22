package it.adesso.management.ordermanagementservice;

import it.adesso.management.ordermanagementservice.DTOs.OrderDTO;
import it.adesso.management.ordermanagementservice.DTOs.SendingEventDTO;
import it.adesso.management.ordermanagementservice.enums.OrderSendingEventEnum;
import it.adesso.management.ordermanagementservice.services.QueueService;
import it.adesso.management.ordermanagementservice.services.impl.OrderServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class OrderManagementServiceApplicationTests {

    @Mock
    private QueueService queueService;

    @InjectMocks
    private OrderServiceImpl orderService;

    @Test
    void createOrderShouldSendCreatedEventToQueue() {
        Long externalId = 10L;

        orderService.createOrder(externalId, new OrderDTO());

        ArgumentCaptor<SendingEventDTO> eventCaptor = ArgumentCaptor.forClass(SendingEventDTO.class);
        verify(queueService).sendMessage(org.mockito.ArgumentMatchers.eq("orderOrchestrationService-out-0"), eventCaptor.capture());

        SendingEventDTO capturedEvent = eventCaptor.getValue();
        assertEquals(externalId, capturedEvent.getOrderId());
        assertEquals(OrderSendingEventEnum.CREATED, capturedEvent.getEvent());
    }
}
