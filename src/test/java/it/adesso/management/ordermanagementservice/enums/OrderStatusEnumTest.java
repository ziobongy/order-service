package it.adesso.management.ordermanagementservice.enums;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class OrderStatusEnumTest {
    @Test
    void testOrderStatusEnumValues() {
        // Assert all enum values exist
        assertNotNull(OrderStatusEnum.DELETED);
        assertNotNull(OrderStatusEnum.COMPLETED);
        assertNotNull(OrderStatusEnum.CHARGED);
        assertNotNull(OrderStatusEnum.PAYED);
        assertNotNull(OrderStatusEnum.UNDER_PAYMENT);
        assertNotNull(OrderStatusEnum.ACQUIRED);
        assertNotNull(OrderStatusEnum.UNDER_EDITING);
        assertNotNull(OrderStatusEnum.CREATED);
    }
    @Test
    void testOrderStatusEnumNames() {
        // Assert
        assertEquals("DELETED", OrderStatusEnum.DELETED.getName());
        assertEquals("COMPLETED", OrderStatusEnum.COMPLETED.getName());
        assertEquals("CHARGED", OrderStatusEnum.CHARGED.getName());
        assertEquals("PAYED", OrderStatusEnum.PAYED.getName());
        assertEquals("UNDER_PAYMENT", OrderStatusEnum.UNDER_PAYMENT.getName());
        assertEquals("ACQUIRED", OrderStatusEnum.ACQUIRED.getName());
        assertEquals("UNDER_EDITING", OrderStatusEnum.UNDER_EDITING.getName());
    }
    @Test
    void testOrderStatusEnumValueOf() {
        // Assert
        assertEquals(OrderStatusEnum.DELETED, OrderStatusEnum.valueOf("DELETED"));
        assertEquals(OrderStatusEnum.CHARGED, OrderStatusEnum.valueOf("CHARGED"));
        assertEquals(OrderStatusEnum.PAYED, OrderStatusEnum.valueOf("PAYED"));
    }
    @Test
    void testOrderStatusEnumValues_Count() {
        // Assert there are 8 enum values
        OrderStatusEnum[] values = OrderStatusEnum.values();
        assertEquals(8, values.length);
    }
    @Test
    void testOrderStatusEnumGetter() {
        // Test that the getter is working
        OrderStatusEnum status = OrderStatusEnum.CHARGED;
        String name = status.getName();
        assertNotNull(name);
        assertEquals("CHARGED", name);
    }
}
