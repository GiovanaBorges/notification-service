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
public class BookingEventService {
    private final SimpMessagingTemplate messaging;
    private static final Logger LOG = LoggerFactory.getLogger(BookingEventService.class.getName());

    public BookingEventService(SimpMessagingTemplate messaging) {
        this.messaging = messaging;
    }

    @CircuitBreaker(name = "bookingListenerCircuitBreaker", fallbackMethod = "handleBookingEventFailure")
    @Retry(name = "bookingListenerRetry", fallbackMethod = "handleBookingEventFailure")
    @RateLimiter(name = "bookingListenerRateLimiter")
    public void handleBookingEvent(NotificationEventDTO dto, String destination,String eventType){
        messaging.convertAndSend(destination, dto);
        LOG.info("Received booking event: {} : {} " , eventType , dto);
    }

    public void handleBookingEventFailure(NotificationEventDTO dto, String destination, String eventType, Throwable throwable) {
        LOG.error("Failed to process booking event: " + dto, throwable);
    }
}
