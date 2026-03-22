package it.adesso.management.ordermanagementservice.services;

import it.adesso.management.ordermanagementservice.DTOs.BFF.OrderBffDTO;
import it.adesso.management.ordermanagementservice.DTOs.OrderDTO;
import it.adesso.management.ordermanagementservice.enums.OrderStatusEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderService {
    Page<OrderBffDTO> findPendingOrders(Pageable pageable);
    OrderBffDTO findById(Long id);
    void createOrder(Long externalId, OrderDTO orderDTO);
    void saveStatusByExternalId(Long externalId, OrderStatusEnum status);
    void updateOrder(Long externalId, OrderDTO orderDTO);
    void deleteOrder(Long externalId);
    void takeCharge(Long externalId);
}
