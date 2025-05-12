package ru.alexandr.BookingCinemaTickets.domain.repository;

import ru.alexandr.BookingCinemaTickets.domain.model.User;

import java.util.List;

public interface UserRepositoryCustom {
    List<User> findByRoleName(String roleName);
}
