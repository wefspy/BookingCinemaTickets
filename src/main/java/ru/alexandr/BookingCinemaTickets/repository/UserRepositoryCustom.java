package ru.alexandr.BookingCinemaTickets.repository;

import ru.alexandr.BookingCinemaTickets.domain.User;

import java.util.List;

public interface UserRepositoryCustom {
    List<User> findByRoleName(String roleName);
}
