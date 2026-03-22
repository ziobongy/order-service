package it.adesso.management.ordermanagementservice.DTOs.BFF;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrderEntryBffDTOTest {

    private OrderEntryBffDTO orderEntryBffDTO;

    @BeforeEach
    void setUp() {
        orderEntryBffDTO = new OrderEntryBffDTO();
        orderEntryBffDTO.setId(1L);
    }

    @Test
    void testOrderEntryBffDTOGettersSetters() {
        // Assert
        assertEquals(1L, orderEntryBffDTO.getId());
    }

    @Test
    void testOrderEntryBffDTOSetId() {
        // Act
        orderEntryBffDTO.setId(2L);

        // Assert
        assertEquals(2L, orderEntryBffDTO.getId());
    }

    @Test
    void testOrderEntryBffDTOMultipleInstances() {
        // Arrange
        OrderEntryBffDTO entry1 = new OrderEntryBffDTO();
        entry1.setId(1L);
        
        OrderEntryBffDTO entry2 = new OrderEntryBffDTO();
        entry2.setId(2L);

        // Assert
        assertEquals(1L, entry1.getId());
        assertEquals(2L, entry2.getId());
        assertNotEquals(entry1.getId(), entry2.getId());
    }
}

