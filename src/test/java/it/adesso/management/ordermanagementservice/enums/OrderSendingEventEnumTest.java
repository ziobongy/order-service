package it.adesso.management.ordermanagementservice.enums;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class OrderSendingEventEnumTest {
    @Test
    void testOrderSendingEventEnumValues() {
        // Assert all enum values exist
        assertNotNull(OrderSendingEventEnum.CREATED);
        assertNotNull(OrderSendingEventEnum.EDITED);
        assertNotNull(OrderSendingEventEnum.DELETED);
        assertNotNull(OrderSendingEventEnum.CHARGED);
        assertNotNull(OrderSendingEventEnum.COMPLETED);
        assertNotNull(OrderSendingEventEnum.PAYED);
    }
    @Test
    void testOrderSendingEventEnumValueOf() {
        // Assert
        assertEquals(OrderSendingEventEnum.CREATED, OrderSendingEventEnum.valueOf("CREATED"));
        assertEquals(OrderSendingEventEnum.EDITED, OrderSendingEventEnum.valueOf("EDITED"));
        assertEquals(OrderSendingEventEnum.DELETED, OrderSendingEventEnum.valueOf("DELETED"));
        assertEquals(OrderSendingEventEnum.CHARGED, OrderSendingEventEnum.valueOf("CHARGED"));
    }
    @Test
    void testOrderSendingEventEnumValues_Count() {
        // Assert there are enum values
        OrderSendingEventEnum[] values = OrderSendingEventEnum.values();
        assertTrue(values.length > 0);
        assertEquals(6, values.length);
    }
    @Test
    void testOrderSendingEventEnumIteration() {
        // Test iteration
        for (OrderSendingEventEnum event : OrderSendingEventEnum.values()) {
            assertNotNull(event);
            assertNotNull(event.name());
        }
    }
}
