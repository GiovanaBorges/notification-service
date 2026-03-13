package com.notification.notification_service.services.listeners;

import org.springframework.amqp.rabbit.annotation.RabbitListener;

import com.notification.notification_service.DTO.NotificationEventDTO;
import com.notification.notification_service.services.events.UserEventService;

public class UserListener {
    private final UserEventService userEventService;
    public UserListener(UserEventService userEventService) {
        this.userEventService = userEventService;
    }

    @RabbitListener(queues = "${rabbitmq.users.queue.created}")
    public void onUserCreateEvent(NotificationEventDTO dto) {
        userEventService.handleUserEvent(dto, "/topic/users/created", "CREATED");
    }
}
