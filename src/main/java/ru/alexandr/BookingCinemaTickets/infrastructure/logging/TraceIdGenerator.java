package ru.alexandr.BookingCinemaTickets.infrastructure.logging;

import java.util.UUID;

public final class TraceIdGenerator {
    private TraceIdGenerator() {}

    public static String generateTraceId() {
        return UUID.randomUUID().toString();
    }
} 