package it.adesso.management.ordermanagementservice.services;

import it.adesso.management.ordermanagementservice.DTOs.OrderDTO;

public interface OrderService {
    void createOrder(Long externalId, OrderDTO orderDTO);
}
