package it.adesso.management.ordermanagementservice.DTOs.BFF;

import it.adesso.management.ordermanagementservice.DTOs.OrderEntryDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderBffDTOTest {

    private OrderBffDTO orderBffDTO;

    @BeforeEach
    void setUp() {
        orderBffDTO = new OrderBffDTO();
        orderBffDTO.setId(1L);
        orderBffDTO.setStatus("PAYED");
        orderBffDTO.setEntries(new ArrayList<>());
    }

    @Test
    void testOrderBffDTOGettersSetters() {
        // Assert initial values
        assertEquals(1L, orderBffDTO.getId());
        assertEquals("PAYED", orderBffDTO.getStatus());
        assertNotNull(orderBffDTO.getEntries());
        assertTrue(orderBffDTO.getEntries().isEmpty());
    }

    @Test
    void testOrderBffDTOSetId() {
        // Act
        orderBffDTO.setId(2L);

        // Assert
        assertEquals(2L, orderBffDTO.getId());
    }

    @Test
    void testOrderBffDTOSetStatus() {
        // Act
        orderBffDTO.setStatus("CHARGED");

        // Assert
        assertEquals("CHARGED", orderBffDTO.getStatus());
    }

    @Test
    void testOrderBffDTOSetEntries() {
        // Arrange
        List<OrderEntryBffDTO> entries = new ArrayList<>();
        OrderEntryBffDTO entry = new OrderEntryBffDTO();
        entry.setId(1L);
        entries.add(entry);

        // Act
        orderBffDTO.setEntries(entries);

        // Assert
        assertEquals(1, orderBffDTO.getEntries().size());
        assertEquals(1L, orderBffDTO.getEntries().get(0).getId());
    }

    @Test
    void testOrderBffDTOMultipleEntries() {
        // Arrange
        List<OrderEntryBffDTO> entries = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            OrderEntryBffDTO entry = new OrderEntryBffDTO();
            entry.setId((long) i);
            entries.add(entry);
        }

        // Act
        orderBffDTO.setEntries(entries);

        // Assert
        assertEquals(5, orderBffDTO.getEntries().size());
        for (int i = 0; i < 5; i++) {
            assertEquals((long) (i + 1), orderBffDTO.getEntries().get(i).getId());
        }
    }
}

