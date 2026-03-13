package com.notification.notification_service.services;

import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.notification.notification_service.DTO.NotificationEventDTO;
import com.notification.notification_service.ENUMS.NotificationTypeENUM;
import com.notification.notification_service.services.events.BookingEventService;
import com.notification.notification_service.services.listeners.BookingListener;

@ExtendWith(MockitoExtension.class)
public class BookingListenerTest {

    @Mock
    private BookingEventService bookingEventService;

    @InjectMocks
    private BookingListener bookingListener;

    @Test
    void testOnBookingCreateEvent() {
        NotificationEventDTO dto = new NotificationEventDTO(NotificationTypeENUM.CREATE, "123", "Booking created");

        bookingListener.onBookingCreateEvent(dto);

        verify(bookingEventService).handleBookingEvent(dto, "/topic/bookings/created", "created");
    }

    @Test
    void testOnBookingUpdateEvent() {
        NotificationEventDTO dto = new NotificationEventDTO(NotificationTypeENUM.UPDATE, "456", "Booking updated");

        bookingListener.onBookingUpdateEvent(dto);

        verify(bookingEventService).handleBookingEvent(dto, "/topic/bookings/updated", "updated");
    }

    @Test
    void testOnBookingDeleteEvent() {
        NotificationEventDTO dto = new NotificationEventDTO(NotificationTypeENUM.DELETE, "789", "Booking deleted");

        bookingListener.onBookingDeleteEvent(dto);

        verify(bookingEventService).handleBookingEvent(dto, "/topic/bookings/deleted", "deleted");
    }
}
