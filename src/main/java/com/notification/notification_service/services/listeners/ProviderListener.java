package com.notification.notification_service.services.listeners;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import com.notification.notification_service.DTO.NotificationEventDTO;
import com.notification.notification_service.services.events.ProviderEventService;

@Service
public class ProviderListener {

    private ProviderEventService providerEventService;

    public ProviderListener(ProviderEventService providerEventService) {
        this.providerEventService = providerEventService;
    }
     @RabbitListener(queues = "${rabbitmq.provider.queue.created}")
    public void onProviderCreateEvent(NotificationEventDTO dto) {
        providerEventService.handleProviderEvent(dto, "/topic/providers/created", "created");
    }

    @RabbitListener(queues = "${rabbitmq.provider.queue.updated}")
    public void onProviderUpdateEvent(NotificationEventDTO dto) {
        providerEventService.handleProviderEvent(dto, "/topic/providers/updated", "updated");
    }

    @RabbitListener(queues = "${rabbitmq.provider.queue.deleted}")
    public void onProviderDeleteEvent(NotificationEventDTO dto) {
        providerEventService.handleProviderEvent(dto, "/topic/providers/deleted", "deleted");
    }
}
