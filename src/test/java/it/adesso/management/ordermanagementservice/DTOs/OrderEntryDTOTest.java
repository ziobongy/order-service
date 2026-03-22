package it.adesso.management.ordermanagementservice.DTOs;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class OrderEntryDTOTest {

    private OrderEntryDTO orderEntryDTO;

    @BeforeEach
    void setUp() {
        orderEntryDTO = OrderEntryDTO.builder()
                .id(1L)
                .orderId(100L)
                .idPizza(1L)
                .idBase(1L)
                .addedIngredients(new HashSet<>(Set.of(1L, 2L)))
                .removedIngredients(new HashSet<>(Set.of(3L)))
                .build();
    }

    @Test
    void testOrderEntryDTOBuilder() {
        // Assert
        assertNotNull(orderEntryDTO);
        assertEquals(1L, orderEntryDTO.getId());
        assertEquals(100L, orderEntryDTO.getOrderId());
        assertEquals(1L, orderEntryDTO.getIdPizza());
        assertEquals(1L, orderEntryDTO.getIdBase());
        assertEquals(2, orderEntryDTO.getAddedIngredients().size());
        assertEquals(1, orderEntryDTO.getRemovedIngredients().size());
    }

    @Test
    void testOrderEntryDTOSetters() {
        // Act
        orderEntryDTO.setId(2L);
        orderEntryDTO.setIdPizza(2L);
        orderEntryDTO.setIdBase(2L);

        // Assert
        assertEquals(2L, orderEntryDTO.getId());
        assertEquals(2L, orderEntryDTO.getIdPizza());
        assertEquals(2L, orderEntryDTO.getIdBase());
    }

    @Test
    void testOrderEntryDTOWithIngredients() {
        // Arrange
        Set<Long> addedIngredients = Set.of(10L, 20L, 30L);
        Set<Long> removedIngredients = Set.of(5L, 6L);

        // Act
        orderEntryDTO.setAddedIngredients(addedIngredients);
        orderEntryDTO.setRemovedIngredients(removedIngredients);

        // Assert
        assertEquals(3, orderEntryDTO.getAddedIngredients().size());
        assertEquals(2, orderEntryDTO.getRemovedIngredients().size());
    }

    @Test
    void testOrderEntryDTONoArgsConstructor() {
        // Act
        OrderEntryDTO newEntry = new OrderEntryDTO();

        // Assert
        assertNotNull(newEntry);
        assertNull(newEntry.getId());
    }

    @Test
    void testOrderEntryDTOAllArgsConstructor() {
        // Arrange
        Set<Long> added = Set.of(1L);
        Set<Long> removed = Set.of(2L);

        // Act
        OrderEntryDTO newEntry = new OrderEntryDTO(5L, 50L, 10L, 10L, added, removed);

        // Assert
        assertEquals(5L, newEntry.getId());
        assertEquals(50L, newEntry.getOrderId());
        assertEquals(10L, newEntry.getIdPizza());
        assertEquals(10L, newEntry.getIdBase());
    }

    @Test
    void testOrderEntryDTOWithNullIngredients() {
        // Act
        orderEntryDTO.setAddedIngredients(null);
        orderEntryDTO.setRemovedIngredients(null);

        // Assert
        assertNull(orderEntryDTO.getAddedIngredients());
        assertNull(orderEntryDTO.getRemovedIngredients());
    }
}

