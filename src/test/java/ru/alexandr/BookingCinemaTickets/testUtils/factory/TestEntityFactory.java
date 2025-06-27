package ru.alexandr.BookingCinemaTickets.testUtils.factory;

import org.springframework.test.util.ReflectionTestUtils;
import ru.alexandr.BookingCinemaTickets.domain.enums.*;
import ru.alexandr.BookingCinemaTickets.domain.model.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class TestEntityFactory {
    public static Genre genre(Long id, String name) {
        Genre genre = new Genre(name);
        setId(genre, id);
        return genre;
    }

    public static GenreMovie genreMovie(Long id,
                                        Movie movie,
                                        Genre genre) {
        GenreMovie genreMovie = new GenreMovie(movie, genre);
        setId(genreMovie, id);
        return genreMovie;
    }

    public static Hall hall(Long id,
                            String name,
                            SoundSystem soundSystem) {
        Hall hall = new Hall(name, soundSystem);
        setId(hall, id);
        return hall;
    }

    public static Movie movie(Long id,
                              String title,
                              Integer durationInMinutes,
                              LocalDate releaseDate,
                              Rating rating) {
        Movie movie = new Movie(title, durationInMinutes, releaseDate, rating);
        setId(movie, id);
        return movie;
    }

    public static Payment payment(Long id,
                                  UserInfo userInfo,
                                  Ticket ticket,
                                  BigDecimal amount,
                                  PaymentStatus status,
                                  LocalDateTime paymentDate) {
        Payment payment = new Payment(userInfo, ticket, amount, status, paymentDate);
        setId(payment, id);
        return payment;
    }

    public static Role role(Long id, String name) {
        Role role = new Role(name);
        setId(role, id);
        return role;
    }

    public static RoleUser roleUser(Long id,
                                    User user,
                                    Role role) {
        RoleUser roleUser = new RoleUser(user, role);
        setId(roleUser, id);
        return roleUser;
    }

    public static Seat seat(Long id,
                            Hall hall,
                            Integer rowNumber,
                            Integer seatNumber,
                            SeatType type) {
        Seat seat = new Seat(hall, rowNumber, seatNumber, type);
        setId(seat, id);
        return seat;
    }

    public static Session session(Long id,
                                  Movie movie,
                                  Hall hall,
                                  LocalDateTime startTime) {
        Session session = new Session(movie, hall, startTime);
        setId(session, id);
        return session;
    }

    public static SessionSeat sessionSeat(Long id,
                                          Session session,
                                          Seat seat,
                                          BigDecimal price,
                                          SessionSeatStatus status) {
        SessionSeat sessionSeat = new SessionSeat(session, seat, price, status);
        setId(sessionSeat, id);
        return sessionSeat;
    }

    public static Ticket ticket(UserInfo userInfo,
                                SessionSeat sessionSeat,
                                LocalDateTime bookingTime,
                                String qrCode,
                                Boolean used) {
        Ticket ticket = new Ticket(userInfo, sessionSeat, bookingTime, qrCode, used);
        setId(ticket, ticket.getId());
        return ticket;
    }

    public static User user(Long id,
                            String username,
                            String password) {
        User user = new User(username, password);
        setId(user, id);
        return user;
    }

    public static UserInfo userInfo(Long id,
                                    User user,
                                    LocalDateTime createdAt) {
        UserInfo userInfo = new UserInfo(user, createdAt);
        setId(userInfo, id);
        return userInfo;
    }

    private static void setId(Object entity, Long id) {
        ReflectionTestUtils.setField(entity, "id", id);
    }


}
