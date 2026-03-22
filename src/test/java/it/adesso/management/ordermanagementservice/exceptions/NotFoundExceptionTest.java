package it.adesso.management.ordermanagementservice.exceptions;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class NotFoundExceptionTest {
    @Test
    void testNotFoundExceptionWithMessage() {
        // Arrange
        String message = "Order not found";
        // Act
        NotFoundException exception = new NotFoundException(message);
        // Assert
        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
        assertTrue(exception instanceof RuntimeException);
    }
    @Test
    void testNotFoundExceptionThrown() {
        // Arrange
        String message = "Resource not found";
        // Act & Assert
        assertThrows(NotFoundException.class, () -> {
            throw new NotFoundException(message);
        });
    }
    @Test
    void testNotFoundExceptionCanBeCaught() {
        // Arrange
        String message = "Test exception";
        NotFoundException exception = new NotFoundException(message);
        // Act & Assert
        try {
            throw exception;
        } catch (RuntimeException e) {
            assertTrue(e instanceof NotFoundException);
            assertEquals(message, e.getMessage());
        }
    }
    @Test
    void testNotFoundExceptionWithDifferentMessages() {
        // Test various messages
        String[] messages = {
                "Order with id 1 not found",
                "User not found",
                "Resource not found",
                ""
        };
        for (String msg : messages) {
            NotFoundException exception = new NotFoundException(msg);
            assertEquals(msg, exception.getMessage());
        }
    }
}
