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
import com.notification.notification_service.services.events.UserEventService;

@ExtendWith(MockitoExtension.class)
public class UserEventServiceTest {

    @Mock
    private SimpMessagingTemplate messaging;

    @InjectMocks
    private UserEventService userEventService;

    @Test
    void shouldSendMessageToWebSocket(){
        NotificationEventDTO dto = new NotificationEventDTO(NotificationTypeENUM.CREATE,"123","User Created");

        userEventService.handleUserEvent(dto, "/topic/user/created", "created");

        verify(messaging).convertAndSend("/topic/user/created", dto);
    }
}
