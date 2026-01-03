package com.notification.notification_service.services.listeners;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.notification.notification_service.DTO.NotificationEventDTO;

@Service
public class ProviderListener {

    private final SimpMessagingTemplate messaging;

    public ProviderListener(SimpMessagingTemplate messaging) {
        this.messaging = messaging;
    }

    @RabbitListener(queues = "${rabbitmq.provider.queue.created}")
    public void onProviderCreateEvent(NotificationEventDTO dto) {
        messaging.convertAndSend("/topic/providers/created", dto);
    }

    @RabbitListener(queues = "${rabbitmq.provider.queue.updated}")
    public void onProviderUpdateEvent(NotificationEventDTO dto) {
        messaging.convertAndSend("/topic/providers/updated", dto);
    }

    @RabbitListener(queues = "${rabbitmq.provider.queue.deleted}")
    public void onProviderDeleteEvent(NotificationEventDTO dto) {
        messaging.convertAndSend("/topic/providers/deleted", dto);
    }

}
