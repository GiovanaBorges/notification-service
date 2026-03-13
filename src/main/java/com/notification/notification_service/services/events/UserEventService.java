package com.notification.notification_service.services.events;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.notification.notification_service.DTO.NotificationEventDTO;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;

@Service
public class UserEventService {
    private final SimpMessagingTemplate messaging;
    private static final Logger LOG = LoggerFactory.getLogger(UserEventService.class.getName());

    public UserEventService(SimpMessagingTemplate messaging) {
        this.messaging = messaging;
    }

    @CircuitBreaker(name = "userListenerCircuitBreaker", fallbackMethod = "handleUserEventFailure")
    @Retry(name = "userListenerRetry", fallbackMethod = "handleUserEventFailure")
    @RateLimiter(name = "userListenerRateLimiter")
    public void handleUserEvent(NotificationEventDTO dto, String destination, String eventType) {
        messaging.convertAndSend(destination, dto);
        LOG.info("Received user {} event: {}", eventType, dto);
    }

    public void handleUserEventFailure(NotificationEventDTO dto, Throwable throwable) {
        LOG.error("Failed to process user event: " + dto, throwable);
    }
}
