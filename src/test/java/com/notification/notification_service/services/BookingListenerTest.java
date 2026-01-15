package com.notification.notification_service.services;

import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import com.notification.notification_service.DTO.NotificationEventDTO;
import com.notification.notification_service.ENUMS.NotificationTypeENUM;
import com.notification.notification_service.services.listeners.BookingListener;

public class BookingListenerTest {
    @Mock
    private SimpMessagingTemplate messaging;

    private BookingListener bookingListener;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        bookingListener = new BookingListener(messaging);
    }

    @Test
    void testOnBookingCreateEvent() {
        NotificationEventDTO dto = new NotificationEventDTO(NotificationTypeENUM.CREATE, "123", "Booking created");

        bookingListener.onBookingCreateEvent(dto);

        verify(messaging).convertAndSend("/topic/bookings/created", dto);
    }

    @Test
    void testOnBookingUpdateEvent() {
        NotificationEventDTO dto = new NotificationEventDTO(NotificationTypeENUM.UPDATE, "456", "Booking updated");

        bookingListener.onBookingUpdateEvent(dto);

        verify(messaging).convertAndSend("/topic/bookings/updated", dto);
    }

    @Test
    void testOnBookingDeleteEvent() {
        NotificationEventDTO dto = new NotificationEventDTO(NotificationTypeENUM.DELETE, "789", "Booking deleted");

        bookingListener.onBookingDeleteEvent(dto);

        verify(messaging).convertAndSend("/topic/bookings/deleted", dto);
    }
}
