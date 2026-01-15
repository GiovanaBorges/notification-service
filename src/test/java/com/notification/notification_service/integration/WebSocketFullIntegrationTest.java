package com.notification.notification_service.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

import com.notification.notification_service.DTO.NotificationEventDTO;
import com.notification.notification_service.ENUMS.NotificationTypeENUM;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.ApplicationContext;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.broker.SimpleBrokerMessageHandler;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class WebSocketFullIntegrationTest {

    @LocalServerPort
    private int port;

    private WebSocketStompClient stompClient;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    private StompSession session;

    private final Map<String, BlockingQueue<NotificationEventDTO>> topicQueues = new HashMap<>();

    @Autowired
    ApplicationContext context;

   
    @BeforeEach
void setup() throws Exception {

    // 1️⃣ Espera broker
    await()
        .atMost(5, TimeUnit.SECONDS)
        .untilAsserted(() -> {
            SimpleBrokerMessageHandler handler =
                context.getBean(SimpleBrokerMessageHandler.class);
            assertThat(handler.isRunning()).isTrue();
        });

    // 2️⃣ WebSocket client
    List<Transport> transports = List.of(
        new WebSocketTransport(new StandardWebSocketClient()));

    SockJsClient sockJsClient = new SockJsClient(transports);

    stompClient = new WebSocketStompClient(sockJsClient);
    stompClient.setMessageConverter(new MappingJackson2MessageConverter());

    session = stompClient
        .connectAsync("ws://localhost:" + port + "/ws", new StompSessionHandlerAdapter() {})
        .get();

    String[] topics = {
        "/topic/bookings/created", "/topic/bookings/updated", "/topic/bookings/deleted",
        "/topic/providers/created", "/topic/providers/updated", "/topic/providers/deleted",
        "/topic/users/created"
    };

    for (String topic : topics) {
        BlockingQueue<NotificationEventDTO> queue = new LinkedBlockingDeque<>();
        topicQueues.put(topic, queue);

        session.subscribe(topic, new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return NotificationEventDTO.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                queue.add((NotificationEventDTO) payload);
            }
        });
    }

    // 3️⃣ Dá tempo pro subscribe registrar (importante no CI)
    Thread.sleep(300);
}

    @Test
    void testAllTopicsReceiveMessages() throws InterruptedException {
        // Envia mensagens para todos os tópicos
        messagingTemplate.convertAndSend("/topic/bookings/created",
                new NotificationEventDTO(NotificationTypeENUM.CREATE, "b1", "Booking created"));
        messagingTemplate.convertAndSend("/topic/bookings/updated",
                new NotificationEventDTO(NotificationTypeENUM.UPDATE, "b2", "Booking updated"));
        messagingTemplate.convertAndSend("/topic/bookings/deleted",
                new NotificationEventDTO(NotificationTypeENUM.DELETE, "b3", "Booking deleted"));

        messagingTemplate.convertAndSend("/topic/providers/created",
                new NotificationEventDTO(NotificationTypeENUM.CREATE, "p1", "Provider created"));
        messagingTemplate.convertAndSend("/topic/providers/updated",
                new NotificationEventDTO(NotificationTypeENUM.UPDATE, "p2", "Provider updated"));
        messagingTemplate.convertAndSend("/topic/providers/deleted",
                new NotificationEventDTO(NotificationTypeENUM.DELETE, "p3", "Provider deleted"));

        messagingTemplate.convertAndSend("/topic/users/created",
                new NotificationEventDTO(NotificationTypeENUM.CREATE, "u1", "User created"));

        // Verifica que cada mensagem foi recebida
        assertReceived("/topic/bookings/created", "b1", "Booking created", NotificationTypeENUM.CREATE);
        assertReceived("/topic/bookings/updated", "b2", "Booking updated", NotificationTypeENUM.UPDATE);
        assertReceived("/topic/bookings/deleted", "b3", "Booking deleted", NotificationTypeENUM.DELETE);

        assertReceived("/topic/providers/created", "p1", "Provider created", NotificationTypeENUM.CREATE);
        assertReceived("/topic/providers/updated", "p2", "Provider updated", NotificationTypeENUM.UPDATE);
        assertReceived("/topic/providers/deleted", "p3", "Provider deleted", NotificationTypeENUM.DELETE);

        assertReceived("/topic/users/created", "u1", "User created", NotificationTypeENUM.CREATE);
    }

    private void assertReceived(String topic, String id, String message, NotificationTypeENUM type)
            throws InterruptedException {
         NotificationEventDTO received =
        await()
            .atMost(5, TimeUnit.SECONDS)
            .until(() -> topicQueues.get(topic).poll(), Objects::nonNull);

    assertThat(received.bookingId()).isEqualTo(id);
    assertThat(received.message()).isEqualTo(message);
    assertThat(received.notification()).isEqualTo(type);
    }
}