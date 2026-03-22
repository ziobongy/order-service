package it.adesso.management.ordermanagementservice.DTOs;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class OrderDTOTest {

    private OrderDTO orderDTO;

    @BeforeEach
    void setUp() {
        orderDTO = OrderDTO.builder()
                .id(1L)
                .entries(new ArrayList<>())
                .build();
    }

    @Test
    void testOrderDTOBuilder() {
        // Assert
        assertNotNull(orderDTO);
        assertEquals(1L, orderDTO.getId());
        assertNotNull(orderDTO.getEntries());
        assertTrue(orderDTO.getEntries().isEmpty());
    }

    @Test
    void testOrderDTOWithEntries() {
        // Arrange
        OrderEntryDTO entryDTO = OrderEntryDTO.builder()
                .id(1L)
                .idPizza(1L)
                .idBase(1L)
                .build();
        List<OrderEntryDTO> entries = List.of(entryDTO);

        // Act
        orderDTO.setEntries(entries);

        // Assert
        assertEquals(1, orderDTO.getEntries().size());
        assertEquals(1L, orderDTO.getEntries().get(0).getId());
    }

    @Test
    void testOrderDTOSetters() {
        // Act
        orderDTO.setId(2L);
        OrderEntryDTO entry = OrderEntryDTO.builder().id(2L).build();
        orderDTO.setEntries(List.of(entry));

        // Assert
        assertEquals(2L, orderDTO.getId());
        assertEquals(1, orderDTO.getEntries().size());
    }

    @Test
    void testOrderDTONoArgsConstructor() {
        // Act
        OrderDTO newOrderDTO = new OrderDTO();

        // Assert
        assertNotNull(newOrderDTO);
        assertNull(newOrderDTO.getId());
    }

    @Test
    void testOrderDTOAllArgsConstructor() {
        // Arrange
        List<OrderEntryDTO> entries = new ArrayList<>();
        entries.add(OrderEntryDTO.builder().id(1L).build());

        // Act
        OrderDTO newOrderDTO = new OrderDTO(1L, entries);

        // Assert
        assertEquals(1L, newOrderDTO.getId());
        assertEquals(1, newOrderDTO.getEntries().size());
    }
}

