package ru.alexandr.BookingCinemaTickets.application.exception;

public class HallNotFoundException extends RuntimeException {
  public HallNotFoundException(String message) {
    super(message);
  }
}
