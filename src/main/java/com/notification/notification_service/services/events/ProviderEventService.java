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
public class ProviderEventService {

    private final SimpMessagingTemplate messaging;
    private static final Logger LOG = LoggerFactory.getLogger(ProviderEventService.class.getName());

    public ProviderEventService(SimpMessagingTemplate messaging) {
        this.messaging = messaging;
    }

    @CircuitBreaker(name = "providerListenerCircuitBreaker", fallbackMethod = "handleProviderEventFailure")
    @Retry(name = "providerListenerRetry", fallbackMethod = "handleProviderEventFailure")
    @RateLimiter(name = "providerListenerRateLimiter")
    public void handleProviderEvent(NotificationEventDTO dto, String destination, String eventType) {
        messaging.convertAndSend(destination, dto);
        LOG.info("Received provider {} event: {}", eventType, dto);
    }

    public void handleProviderEventFailure(NotificationEventDTO dto, Throwable throwable) {
        LOG.error("Failed to process provider event: " + dto, throwable);
    }

}
