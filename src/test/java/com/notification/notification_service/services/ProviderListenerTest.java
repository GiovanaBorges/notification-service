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

import com.notification.notification_service.DTO.NotificationEventDTO;
import com.notification.notification_service.ENUMS.NotificationTypeENUM;
import com.notification.notification_service.services.events.ProviderEventService;
import com.notification.notification_service.services.listeners.ProviderListener;

@ExtendWith(MockitoExtension.class)
public class ProviderListenerTest {
    @Mock
    private ProviderEventService providerEventService;

    @InjectMocks
    private ProviderListener providerListener;

    @Test
    void testOnProviderCreateEvent() {
        NotificationEventDTO dto = new NotificationEventDTO(NotificationTypeENUM.CREATE, "101", "Provider created");

        providerListener.onProviderCreateEvent(dto);

        // Garante que nenhuma outra fila foi chamada
        verify(providerEventService).handleProviderEvent(dto, "/topic/providers/created", "created");

    }

    @Test
    void testOnProviderUpdateEvent() {
        NotificationEventDTO dto = new NotificationEventDTO(NotificationTypeENUM.UPDATE, "202", "Provider updated");

        providerListener.onProviderUpdateEvent(dto);

        verify(providerEventService).handleProviderEvent(dto, "/topic/providers/updated", "updated");
    }

    @Test
    void testOnProviderDeleteEvent() {
        NotificationEventDTO dto = new NotificationEventDTO(NotificationTypeENUM.DELETE, "303", "Provider deleted");

        providerListener.onProviderDeleteEvent(dto);

        verify(providerEventService).handleProviderEvent(dto, "/topic/providers/deleted", "deleted");
    }
}
