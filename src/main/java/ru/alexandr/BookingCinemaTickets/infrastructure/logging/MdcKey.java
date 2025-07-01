package ru.alexandr.BookingCinemaTickets.infrastructure.logging;

public interface MdcKey {
    String TRACE_ID = "traceId";
    String USER_ID = "userId";
    String CLIENT_IP = "clientIp";
    String SESSION_ID = "sessionId";
}
