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
import com.notification.notification_service.services.listeners.ProviderListener;

public class ProviderListenerTest {
    @Mock
    private SimpMessagingTemplate messaging;

    private ProviderListener providerListener;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        providerListener = new ProviderListener(messaging);
    }

    @Test
    void testOnProviderCreateEvent() {
        NotificationEventDTO dto = new NotificationEventDTO(NotificationTypeENUM.CREATE, "101", "Provider created");

        providerListener.onProviderCreateEvent(dto);

        // Verifica envio correto
        verify(messaging).convertAndSend("/topic/providers/created", dto);

        // Garante que nenhuma outra fila foi chamada
        verifyNoMoreInteractions(messaging);
    }

    @Test
    void testOnProviderUpdateEvent() {
        NotificationEventDTO dto = new NotificationEventDTO(NotificationTypeENUM.UPDATE, "202", "Provider updated");

        providerListener.onProviderUpdateEvent(dto);

        verify(messaging).convertAndSend("/topic/providers/updated", dto);
        verifyNoMoreInteractions(messaging);
    }

    @Test
    void testOnProviderDeleteEvent() {
        NotificationEventDTO dto = new NotificationEventDTO(NotificationTypeENUM.DELETE, "303", "Provider deleted");

        providerListener.onProviderDeleteEvent(dto);

        verify(messaging).convertAndSend("/topic/providers/deleted", dto);
        verifyNoMoreInteractions(messaging);
    }
}
