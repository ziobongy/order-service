package it.adesso.management.ordermanagementservice.services;


import it.adesso.management.ordermanagementservice.DTOs.SendingEventDTO;

public interface QueueService {
    void sendMessage(String bindingName, SendingEventDTO sendingEventDTO);
}
