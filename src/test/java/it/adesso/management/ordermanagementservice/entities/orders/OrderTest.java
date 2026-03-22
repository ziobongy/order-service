package it.adesso.management.ordermanagementservice.entities.orders;

import it.adesso.management.ordermanagementservice.enums.OrderStatusEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    private Order order;

    @BeforeEach
    void setUp() {
        order = Order.builder()
                .id(1L)
                .externalId(100L)
                .placedBy("testUser")
                .status(OrderStatusEnum.PAYED.getName())
                .chargedBy(null)
                .entries(new HashSet<>())
                .createdAt(Instant.now())
                .build();
    }

    @Test
    void testOrderBuilder() {
        // Assert
        assertNotNull(order);
        assertEquals(1L, order.getId());
        assertEquals(100L, order.getExternalId());
        assertEquals("testUser", order.getPlacedBy());
        assertEquals(OrderStatusEnum.PAYED.getName(), order.getStatus());
        assertNull(order.getChargedBy());
        assertNotNull(order.getCreatedAt());
    }

    @Test
    void testOrderSetters() {
        // Act
        order.setStatus(OrderStatusEnum.CHARGED.getName());
        order.setChargedBy("chargedByUser");

        // Assert
        assertEquals(OrderStatusEnum.CHARGED.getName(), order.getStatus());
        assertEquals("chargedByUser", order.getChargedBy());
    }

    @Test
    void testOrderWithEntries() {
        // Arrange
        OrderEntry entry = OrderEntry.builder().id(1L).order(order).build();
        Set<OrderEntry> entries = Set.of(entry);

        // Act
        order.setEntries(entries);

        // Assert
        assertEquals(1, order.getEntries().size());
        assertTrue(order.getEntries().contains(entry));
    }

    @Test
    void testOrderEquality() {
        // Arrange
        Order order2 = Order.builder()
                .id(1L)
                .externalId(100L)
                .placedBy("testUser")
                .status(OrderStatusEnum.PAYED.getName())
                .chargedBy(null)
                .entries(new HashSet<>())
                .createdAt(order.getCreatedAt())
                .build();

        // Assert - Note: Equality depends on implementation
        assertNotNull(order2);
        assertEquals(order.getId(), order2.getId());
        assertEquals(order.getExternalId(), order2.getExternalId());
    }

    @Test
    void testOrderNoArgsConstructor() {
        // Act
        Order newOrder = new Order();

        // Assert
        assertNotNull(newOrder);
        assertNull(newOrder.getId());
    }
}

