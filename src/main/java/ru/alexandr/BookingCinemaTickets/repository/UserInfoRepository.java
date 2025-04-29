package ru.alexandr.BookingCinemaTickets.repository;

import org.springframework.data.repository.CrudRepository;
import ru.alexandr.BookingCinemaTickets.domain.UserInfo;

import java.util.Optional;

public interface UserInfoRepository extends CrudRepository<UserInfo, Long> {
    Optional<UserInfo> findByEmail(String email);
}
