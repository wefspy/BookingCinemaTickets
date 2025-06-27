package ru.alexandr.BookingCinemaTickets.testUtils.constant;

public interface RestControllerUrls {
    String BASE_REST_URL = "/api";

    String REGISTRATION = BASE_REST_URL + "/registration";

    String AUTH_CONTROLLER = BASE_REST_URL + "/auth";
    String AUTH_LOGIN = AUTH_CONTROLLER + "/login";
    String AUTH_REFRESH = AUTH_CONTROLLER + "/refresh";
}
