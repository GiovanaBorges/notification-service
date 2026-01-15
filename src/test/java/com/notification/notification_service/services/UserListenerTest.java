package com.notification.notification_service.services;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import com.notification.notification_service.DTO.NotificationEventDTO;
import com.notification.notification_service.ENUMS.NotificationTypeENUM;
import com.notification.notification_service.services.listeners.UserListener;

public class UserListenerTest {
    @Mock
    private SimpMessagingTemplate messaging;

    private UserListener userListener;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userListener = new UserListener(messaging);
    }

    @Test
    void testOnUserCreateEvent() {
        NotificationEventDTO dto = new NotificationEventDTO(NotificationTypeENUM.CREATE, "user123", "User created");

        userListener.onUserCreateEvent(dto);

        verify(messaging).convertAndSend("/topic/users/created", dto);

        verifyNoMoreInteractions(messaging);
    }
}
