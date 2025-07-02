package ru.alexandr.BookingCinemaTickets.application.exception;

public class SeatNotFoundException extends RuntimeException {
  public SeatNotFoundException(String message) {
    super(message);
  }
}
