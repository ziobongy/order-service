package it.adesso.management.ordermanagementservice.enums;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class ActionEventEnumTest {
    @Test
    void testActionEventEnumValues() {
        // Assert all enum values exist
        assertNotNull(ActionEventEnum.CREATE);
        assertNotNull(ActionEventEnum.UPDATE);
        assertNotNull(ActionEventEnum.UPDATE_PAYED);
        assertNotNull(ActionEventEnum.PAY);
        assertNotNull(ActionEventEnum.REFUND);
        assertNotNull(ActionEventEnum.DELETE);
        assertNotNull(ActionEventEnum.ERROR);
    }
    @Test
    void testActionEventEnumValueOf() {
        // Assert
        assertEquals(ActionEventEnum.CREATE, ActionEventEnum.valueOf("CREATE"));
        assertEquals(ActionEventEnum.UPDATE, ActionEventEnum.valueOf("UPDATE"));
        assertEquals(ActionEventEnum.DELETE, ActionEventEnum.valueOf("DELETE"));
    }
    @Test
    void testActionEventEnumValues_Count() {
        // Assert there are 7 enum values
        ActionEventEnum[] values = ActionEventEnum.values();
        assertEquals(7, values.length);
    }
    @Test
    void testActionEventEnumIteration() {
        // Test iteration
        for (ActionEventEnum event : ActionEventEnum.values()) {
            assertNotNull(event);
            assertNotNull(event.name());
        }
    }
}
