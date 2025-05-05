package ru.alexandr.BookingCinemaTickets.application.exception;

public class ClaimNotFoundException extends RuntimeException {
    public ClaimNotFoundException(String message) {
        super(message);
    }
}
