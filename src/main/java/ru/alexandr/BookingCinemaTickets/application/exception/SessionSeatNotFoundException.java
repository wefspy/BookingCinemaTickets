package ru.alexandr.BookingCinemaTickets.application.exception;

public class SessionSeatNotFoundException extends RuntimeException {
    public SessionSeatNotFoundException(String message) {
        super(message);
    }
}
