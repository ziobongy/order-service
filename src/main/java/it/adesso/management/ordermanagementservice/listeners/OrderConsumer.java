package it.adesso.management.ordermanagementservice.listeners;

import com.google.gson.Gson;
import it.adesso.management.ordermanagementservice.DTOs.ReceivingEventDTO;
import it.adesso.management.ordermanagementservice.enums.OrderStatusEnum;
import it.adesso.management.ordermanagementservice.services.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@RequiredArgsConstructor
@Slf4j
@Configuration
public class OrderConsumer {

    private final Gson gson;
    private final OrderService orderService;

    @Bean
    public Consumer<String> orderManagement() {
        return order -> {
            ReceivingEventDTO receivedEventDTO;
            try {
                receivedEventDTO = this.gson.fromJson(order, ReceivingEventDTO.class);
            } catch (Exception e) {
                log.error("Error parsing order: {}", e.getMessage());
                return;
            }
            log.info("Received event for order: {} with event: {}", receivedEventDTO.getOrderId(), receivedEventDTO.getEvent());
            switch (receivedEventDTO.getEvent()) {
                case CREATE -> this.orderService.createOrder(receivedEventDTO.getOrderId(), receivedEventDTO.getOrder());
                case UPDATE_PAYED -> this.orderService.saveStatusByExternalId(receivedEventDTO.getOrderId(), OrderStatusEnum.PAYED);
                case UPDATE -> this.orderService.updateOrder(receivedEventDTO.getOrderId(), receivedEventDTO.getOrder());
                case DELETE -> this.orderService.deleteOrder(receivedEventDTO.getOrderId());
            }
        };
    }

    @Bean
    public Consumer<String> orderManagementError() {
        return order -> {
            ReceivingEventDTO receivedEventDTO;
            try {
                receivedEventDTO = this.gson.fromJson(order, ReceivingEventDTO.class);
            } catch (Exception e) {
                log.error("Error parsing order: " + e.getMessage());
                return;
            }
            log.info("Received error event for order: {}", receivedEventDTO.getOrderId());
        };
    }
}
