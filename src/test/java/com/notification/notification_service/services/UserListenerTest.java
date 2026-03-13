package com.notification.notification_service.services;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

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
import com.notification.notification_service.services.listeners.UserListener;

@ExtendWith(MockitoExtension.class)
public class UserListenerTest {

    @InjectMocks
    private UserListener userListener;

    @Mock
    private UserEventService userEventService;

    @Test
    void testOnUserCreateEvent() {
        NotificationEventDTO dto = new NotificationEventDTO(NotificationTypeENUM.CREATE, "user123", "User created");

        userListener.onUserCreateEvent(dto);

        verify(userEventService).handleUserEvent(dto,"/topic/users/created", "CREATED");
    }
}
