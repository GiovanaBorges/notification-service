# Notification Service

![Java](https://img.shields.io/badge/Java-21-blue)
![Spring Boot](https://img.shields.io/badge/SpringBoot-3.x-brightgreen)
![Architecture](https://img.shields.io/badge/Architecture-Event%20Driven-blue)
![RabbitMQ](https://img.shields.io/badge/RabbitMQ-Message%20Broker-orange)
![WebSocket](https://img.shields.io/badge/WebSocket-Realtime-purple)
![Resilience4j](https://img.shields.io/badge/Resilience4j-CircuitBreaker-red)
![Testcontainers](https://img.shields.io/badge/Testcontainers-Integration%20Testing-2496ED)
![Build](https://img.shields.io/badge/build-passing-brightgreen)
[![codecov](https://codecov.io/gh/GiovanaBorges/notification-service/branch/main/graph/badge.svg)](https://codecov.io/gh/GiovanaBorges/notification-service)

A **real-time notification service** built with **Spring Boot** that consumes events from **RabbitMQ** and delivers them to frontend clients through **WebSocket connections**.

This service is part of a **booking platform** where users can schedule sessions with other users (for example online therapy sessions, mentoring, or consultations).

When booking-related events occur, notifications are delivered instantly to connected clients.

---

# Overview

In a booking platform, multiple events can occur during the lifecycle of an appointment:

* a booking is created
* a booking is confirmed
* a booking is canceled
* a reminder needs to be sent before a session

These events are produced by the **Booking Service** and published to **RabbitMQ**.

The **Notification Service** listens to these events and pushes them to connected clients in real time using **WebSocket**.

This avoids the need for the frontend to constantly poll the backend for updates and enables instant user feedback.

Example:

A user schedules a therapy session with a professional.
When the booking is confirmed, an event is published to RabbitMQ and the Notification Service sends the notification instantly to the relevant clients.

---

# Architecture

The service acts as a bridge between the messaging system and the frontend.

```id="swq3i0"
Booking Service
        │
        ▼
     RabbitMQ
        │
        ▼
Notification Service
        │
        ▼
   WebSocket Clients
```

### Event Flow

1. The **Booking Service** publishes a booking event to RabbitMQ
2. The **Notification Service** consumes the message
3. The message is processed
4. A notification is sent to connected WebSocket clients

This architecture ensures **loose coupling between services** and enables scalable event processing.

---

# Tech Stack

* **Java 21**
* **Spring Boot**
* **Spring AMQP (RabbitMQ integration)**
* **Spring WebSocket**
* **Spring Cloud Circuit Breaker (Resilience4j)**
* **Spring Boot Actuator**
* **Jackson**
* **Lombok**

Testing stack:

* **JUnit 5**
* **Testcontainers**
* **Awaitility**
* **Spring AMQP Test utilities**

---

# Key Features

## Event-driven communication

The service consumes messages from RabbitMQ published by other backend services.

This enables asynchronous communication between services and decouples the notification logic from the booking domain.

---

## Real-time notifications with WebSocket

WebSocket allows the server to push updates to connected clients without requiring repeated HTTP requests.

Benefits:

* instant updates
* lower latency
* reduced network overhead
* efficient real-time communication

This is particularly useful for applications that require immediate feedback for user actions, such as booking confirmations or cancellations.

---

## Resilience with Resilience4j

The service uses **Resilience4j Circuit Breaker** to protect the system from cascading failures.

If a dependent component becomes unstable, the circuit breaker helps the system degrade gracefully and recover automatically.

---

## Observability with Actuator

Spring Boot Actuator provides operational insights and monitoring endpoints such as:

* health checks
* application metrics
* runtime information

This improves production readiness and operational visibility.

---

# Testing Strategy

The project includes **integration tests using Testcontainers**.

During test execution, Testcontainers starts a **RabbitMQ container** automatically, allowing the application to interact with a real message broker.

Benefits:

* tests run in an environment closer to production
* no need for locally installed RabbitMQ
* reproducible and isolated test environments

Additional tools used:

* **JUnit 5**
* **Awaitility** for asynchronous assertions
* **Spring AMQP test utilities**

---

# Running the Project

## Requirements

* Java 21
* Maven
* Docker (required for Testcontainers during tests)

---

## Build

```
mvn clean install
```

---

## Run

```
mvn spring-boot:run
```

---

# Example Event

Example message published by the Booking Service:

```json
{
  "bookingId": "b123",
  "userId": "u456",
  "type": "BOOKING_CONFIRMED",
  "message": "Your session has been confirmed",
  "timestamp": "2026-03-13T12:30:00Z"
}
```

When this message is received, the Notification Service pushes the notification to connected WebSocket clients.

---

# Future Improvements

Potential improvements for the service:

* authentication for WebSocket connections
* user-specific notification routing
* notification persistence
* dead-letter queues for failed messages
* load testing using k6
* monitoring dashboards with Prometheus and Grafana

---

# Learning Goals

This project demonstrates important backend engineering concepts:

* event-driven architecture
* asynchronous messaging
* real-time communication
* resilience patterns
* containerized integration testing
