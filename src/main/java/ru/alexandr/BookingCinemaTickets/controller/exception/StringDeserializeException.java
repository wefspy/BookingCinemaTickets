package ru.alexandr.BookingCinemaTickets.controller.exception;

public class StringDeserializeException extends Exception {
    public StringDeserializeException(String message) {
        super(message);
    }
}
