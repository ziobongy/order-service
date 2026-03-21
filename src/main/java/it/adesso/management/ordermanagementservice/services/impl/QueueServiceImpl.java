package it.adesso.management.ordermanagementservice.services.impl;

import it.adesso.management.ordermanagementservice.DTOs.SendingEventDTO;
import it.adesso.management.ordermanagementservice.services.QueueService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class QueueServiceImpl implements QueueService {
    private final StreamBridge streamBridge;

    @Override
    public void sendMessage(String bindingName, SendingEventDTO sendingEventDTO) {
        try {
            log.info("Sending order message to {}: {}", bindingName, sendingEventDTO);
            boolean sent = this.streamBridge.send(
                bindingName,
                sendingEventDTO
            );
            log.info("Message sent successfully: {}", sent);
        } catch (Exception e) {
            log.error("Error sending message to queue", e);
            throw e;
        }
    }
}
