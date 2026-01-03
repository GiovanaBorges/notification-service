package com.notification.notification_service.services.listeners;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.notification.notification_service.DTO.NotificationEventDTO;

@Service
public class UserListener {
     private final SimpMessagingTemplate messaging;

    public UserListener(SimpMessagingTemplate messaging) {
        this.messaging = messaging;
    }

    @RabbitListener(queues = "${rabbitmq.user.queue.created}")
    public void onUserCreateEvent(NotificationEventDTO dto) {
        messaging.convertAndSend("/topic/users/created", dto);
    }
}
