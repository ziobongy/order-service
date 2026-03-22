package it.adesso.management.ordermanagementservice.controllers;

import it.adesso.management.ordermanagementservice.DTOs.BFF.OrderBffDTO;
import it.adesso.management.ordermanagementservice.DTOs.OrderDTO;
import it.adesso.management.ordermanagementservice.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/orders")
public class OrdersController {
    private final OrderService orderService;

    @GetMapping("chargeable")
    public ResponseEntity<Page<OrderBffDTO>> findPending(Pageable pageable){
        return ResponseEntity.ok(this.orderService.findPendingOrders(pageable));
    }

    @PostMapping("{id}/take-charge")
    public ResponseEntity<Void> takeCharge(
        @PathVariable("id") Long orderId
    ) {
        this.orderService.takeCharge(orderId);
        return ResponseEntity.accepted().build();
    }

    @GetMapping("{id}")
    public ResponseEntity<OrderBffDTO> findById(
        @PathVariable("id") Long orderId
    ) {
        return ResponseEntity.ok(this.orderService.findById(orderId));
    }
}
