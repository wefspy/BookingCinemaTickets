package ru.alexandr.BookingCinemaTickets.repository;

import org.springframework.data.repository.CrudRepository;
import ru.alexandr.BookingCinemaTickets.domain.UserInfo;

import java.util.Optional;
import java.util.UUID;

public interface UserInfoRepository extends CrudRepository<UserInfo, UUID> {
    Optional<UserInfo> findByEmail(String email);
}
