package it.adesso.management.ordermanagementservice.controllers;

import it.adesso.management.ordermanagementservice.DTOs.BFF.OrderBffDTO;
import it.adesso.management.ordermanagementservice.services.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrdersControllerTest {

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrdersController ordersController;

    private OrderBffDTO orderBffDTO;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        orderBffDTO = new OrderBffDTO();
        orderBffDTO.setId(1L);
        orderBffDTO.setStatus("PAYED");
        orderBffDTO.setEntries(new ArrayList<>());

        pageable = PageRequest.of(0, 10);
    }

    @Test
    void testFindPending_Success() {
        // Arrange
        List<OrderBffDTO> orders = List.of(orderBffDTO);
        Page<OrderBffDTO> expectedPage = new PageImpl<>(orders, pageable, 1);
        when(orderService.findPendingOrders(any(Pageable.class)))
                .thenReturn(expectedPage);

        // Act
        ResponseEntity<Page<OrderBffDTO>> response = ordersController.findPending(pageable);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().getContent().size());
        assertEquals(1L, response.getBody().getContent().get(0).getId());
        verify(orderService, times(1)).findPendingOrders(pageable);
    }

    @Test
    void testFindPending_EmptyList() {
        // Arrange
        Page<OrderBffDTO> emptyPage = new PageImpl<>(new ArrayList<>(), pageable, 0);
        when(orderService.findPendingOrders(any(Pageable.class)))
                .thenReturn(emptyPage);

        // Act
        ResponseEntity<Page<OrderBffDTO>> response = ordersController.findPending(pageable);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().getContent().isEmpty());
        verify(orderService, times(1)).findPendingOrders(pageable);
    }

    @Test
    void testTakeCharge_Success() {
        // Arrange
        Long orderId = 1L;
        doNothing().when(orderService).takeCharge(anyLong());

        // Act
        ResponseEntity<Void> response = ordersController.takeCharge(orderId);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        verify(orderService, times(1)).takeCharge(orderId);
    }

    @Test
    void testTakeCharge_WithDifferentId() {
        // Arrange
        Long orderId = 42L;
        doNothing().when(orderService).takeCharge(anyLong());

        // Act
        ResponseEntity<Void> response = ordersController.takeCharge(orderId);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        verify(orderService, times(1)).takeCharge(42L);
    }

    @Test
    void testFindById_Success() {
        // Arrange
        Long orderId = 1L;
        when(orderService.findById(anyLong()))
                .thenReturn(orderBffDTO);

        // Act
        ResponseEntity<OrderBffDTO> response = ordersController.findById(orderId);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1L, response.getBody().getId());
        assertEquals("PAYED", response.getBody().getStatus());
        verify(orderService, times(1)).findById(orderId);
    }

    @Test
    void testFindById_DifferentOrder() {
        // Arrange
        OrderBffDTO order2 = new OrderBffDTO();
        order2.setId(2L);
        order2.setStatus("CHARGED");
        when(orderService.findById(2L)).thenReturn(order2);

        // Act
        ResponseEntity<OrderBffDTO> response = ordersController.findById(2L);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2L, response.getBody().getId());
        assertEquals("CHARGED", response.getBody().getStatus());
        verify(orderService, times(1)).findById(2L);
    }
}

