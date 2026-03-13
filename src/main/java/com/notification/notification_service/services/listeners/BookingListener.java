package com.notification.notification_service.services.listeners;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import com.notification.notification_service.DTO.NotificationEventDTO;
import com.notification.notification_service.services.events.BookingEventService;


@Service
public class BookingListener {
    private final BookingEventService bookingEventService;

    public BookingListener(BookingEventService bookingEventService) {
        this.bookingEventService = bookingEventService;
    }

    @RabbitListener(queues = "${rabbitmq.booking.queue.created}")
    public void onBookingCreateEvent(NotificationEventDTO dto) {
        bookingEventService.handleBookingEvent(dto, "/topic/bookings/created", "created");
    }

    @RabbitListener(queues = "${rabbitmq.booking.queue.updated}")
    public void onBookingUpdateEvent(NotificationEventDTO dto) {
        bookingEventService.handleBookingEvent(dto, "/topic/bookings/updated", "updated");
    }

    @RabbitListener(queues = "${rabbitmq.booking.queue.deleted}")
    public void onBookingDeleteEvent(NotificationEventDTO dto) {
        bookingEventService.handleBookingEvent(dto, "/topic/bookings/deleted", "deleted");
    }
}
