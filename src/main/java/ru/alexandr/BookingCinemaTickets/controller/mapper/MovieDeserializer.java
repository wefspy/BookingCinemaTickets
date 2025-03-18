package ru.alexandr.BookingCinemaTickets.controller.mapper;

import ru.alexandr.BookingCinemaTickets.controller.exception.StringDeserializeException;
import ru.alexandr.BookingCinemaTickets.domain.Movie;
import ru.alexandr.BookingCinemaTickets.domain.enums.Rating;
import ru.alexandr.BookingCinemaTickets.service.dto.MovieDto;

import java.net.URI;
import java.time.LocalDateTime;

public final class MovieDeserializer {
    private MovieDeserializer() {}

    public static Movie toMovie(final String[] cmd) throws StringDeserializeException {
        try {
            return new Movie(
                    Long.valueOf(cmd[1]),
                    cmd[2],
                    cmd[3],
                    Integer.valueOf(cmd[4]),
                    LocalDateTime.parse(cmd[5]),
                    Rating.valueOf(cmd[6]),
                    URI.create(cmd[7]).toURL()
            );
        } catch (Exception e) {
            throw new StringDeserializeException(e.getMessage());
        }
    }

    public static MovieDto toDto(final String[] cmd) throws StringDeserializeException {
        try {
            return new MovieDto(
                    cmd[1],
                    cmd[2],
                    Integer.valueOf(cmd[3]),
                    LocalDateTime.parse(cmd[4]),
                    Rating.valueOf(cmd[5]),
                    URI.create(cmd[6]).toURL()
            );
        } catch (Exception e) {
            throw new StringDeserializeException(e.getMessage());
        }
    }
}
