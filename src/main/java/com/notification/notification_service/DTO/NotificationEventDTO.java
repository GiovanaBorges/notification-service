package com.notification.notification_service.DTO;

import com.notification.notification_service.ENUMS.NotificationTypeENUM;

public record NotificationEventDTO(
    NotificationTypeENUM notification,
    String bookingId,
    String message
) {}
