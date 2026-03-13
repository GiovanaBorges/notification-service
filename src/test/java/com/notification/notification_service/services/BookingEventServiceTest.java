package com.notification.notification_service.services;

import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;


import com.notification.notification_service.DTO.NotificationEventDTO;
import com.notification.notification_service.ENUMS.NotificationTypeENUM;

import com.notification.notification_service.services.events.BookingEventService;

@ExtendWith(MockitoExtension.class)
class BookingEventServiceTest{
    
    @Mock
    private SimpMessagingTemplate messaging;

    @InjectMocks
    private BookingEventService bookingEventService;

    @Test
    void shouldSendMessageToWebSocket(){
        NotificationEventDTO dto = new NotificationEventDTO(NotificationTypeENUM.CREATE,"123","Booking Created");

        bookingEventService.handleBookingEvent(dto, "/topic/bookings/created", "created");

        verify(messaging).convertAndSend("/topic/bookings/created", dto);
    }
}