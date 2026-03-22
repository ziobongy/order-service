package it.adesso.management.ordermanagementservice.DTOs;

import it.adesso.management.ordermanagementservice.enums.OrderSendingEventEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SendingEventDTOTest {

    private SendingEventDTO sendingEventDTO;

    @BeforeEach
    void setUp() {
        sendingEventDTO = SendingEventDTO.builder()
                .orderId(100L)
                .event(OrderSendingEventEnum.CREATED)
                .message("Order created successfully")
                .build();
    }

    @Test
    void testSendingEventDTOBuilder() {
        // Assert
        assertNotNull(sendingEventDTO);
        assertEquals(100L, sendingEventDTO.getOrderId());
        assertEquals(OrderSendingEventEnum.CREATED, sendingEventDTO.getEvent());
        assertEquals("Order created successfully", sendingEventDTO.getMessage());
    }

    @Test
    void testSendingEventDTOSetters() {
        // Act
        sendingEventDTO.setOrderId(200L);
        sendingEventDTO.setEvent(OrderSendingEventEnum.CHARGED);
        sendingEventDTO.setMessage("Order charged");

        // Assert
        assertEquals(200L, sendingEventDTO.getOrderId());
        assertEquals(OrderSendingEventEnum.CHARGED, sendingEventDTO.getEvent());
        assertEquals("Order charged", sendingEventDTO.getMessage());
    }

    @Test
    void testSendingEventDTOWithDifferentEvents() {
        // Test all event types
        OrderSendingEventEnum[] events = {
                OrderSendingEventEnum.CREATED,
                OrderSendingEventEnum.EDITED,
                OrderSendingEventEnum.DELETED,
                OrderSendingEventEnum.CHARGED
        };

        for (OrderSendingEventEnum event : events) {
            SendingEventDTO dto = SendingEventDTO.builder()
                    .orderId(1L)
                    .event(event)
                    .build();
            assertEquals(event, dto.getEvent());
        }
    }

    @Test
    void testSendingEventDTONullMessage() {
        // Act
        sendingEventDTO.setMessage(null);

        // Assert
        assertNull(sendingEventDTO.getMessage());
        assertEquals(100L, sendingEventDTO.getOrderId());
    }

    @Test
    void testSendingEventDTOWithoutMessage() {
        // Arrange
        SendingEventDTO dto = SendingEventDTO.builder()
                .orderId(100L)
                .event(OrderSendingEventEnum.DELETED)
                .build();

        // Assert
        assertNull(dto.getMessage());
        assertEquals(100L, dto.getOrderId());
        assertEquals(OrderSendingEventEnum.DELETED, dto.getEvent());
    }
}

