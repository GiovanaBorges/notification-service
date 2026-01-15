package com.notification.notification_service.services.listeners;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.notification.notification_service.DTO.NotificationEventDTO;

@Service
public class BookingListener {
     private final SimpMessagingTemplate messaging;

    public BookingListener(SimpMessagingTemplate messaging) {
        this.messaging = messaging;
    }

    @RabbitListener(queues = "${rabbitmq.booking.queue.created}")
    public void onBookingCreateEvent(NotificationEventDTO dto) {
        messaging.convertAndSend("/topic/bookings/created", dto);
    }

    @RabbitListener(queues = "${rabbitmq.booking.queue.updated}")
    public void onBookingUpdateEvent(NotificationEventDTO dto) {
        messaging.convertAndSend("/topic/bookings/updated", dto);
    }

    @RabbitListener(queues = "${rabbitmq.booking.queue.deleted}")
    public void onBookingDeleteEvent(NotificationEventDTO dto) {
        messaging.convertAndSend("/topic/bookings/deleted", dto);
    }
}
