package it.adesso.management.ordermanagementservice.DTOs;
import it.adesso.management.ordermanagementservice.enums.ActionEventEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class ReceivingEventDTOTest {
    private ReceivingEventDTO receivingEventDTO;
    @BeforeEach
    void setUp() {
        receivingEventDTO = ReceivingEventDTO.builder()
                .orderId(100L)
                .event(ActionEventEnum.CREATE)
                .build();
    }
    @Test
    void testReceivingEventDTOBuilder() {
        // Assert
        assertNotNull(receivingEventDTO);
        assertEquals(100L, receivingEventDTO.getOrderId());
        assertEquals(ActionEventEnum.CREATE, receivingEventDTO.getEvent());
    }
    @Test
    void testReceivingEventDTOSetters() {
        // Act
        receivingEventDTO.setOrderId(200L);
        receivingEventDTO.setEvent(ActionEventEnum.UPDATE);
        // Assert
        assertEquals(200L, receivingEventDTO.getOrderId());
        assertEquals(ActionEventEnum.UPDATE, receivingEventDTO.getEvent());
    }
    @Test
    void testReceivingEventDTOWithDifferentEvents() {
        // Arrange & Act
        ReceivingEventDTO dto1 = ReceivingEventDTO.builder().orderId(1L).event(ActionEventEnum.DELETE).build();
        ReceivingEventDTO dto2 = ReceivingEventDTO.builder().orderId(2L).event(ActionEventEnum.PAY).build();
        // Assert
        assertEquals(ActionEventEnum.DELETE, dto1.getEvent());
        assertEquals(ActionEventEnum.PAY, dto2.getEvent());
    }
    @Test
    void testReceivingEventDTOWithMessage() {
        // Act
        receivingEventDTO.setMessage("Event message");
        // Assert
        assertEquals("Event message", receivingEventDTO.getMessage());
    }
    @Test
    void testReceivingEventDTOWithOrder() {
        // Arrange
        OrderDTO orderDTO = OrderDTO.builder().id(1L).build();
        // Act
        receivingEventDTO.setOrder(orderDTO);
        // Assert
        assertEquals(1L, receivingEventDTO.getOrder().getId());
    }
}
