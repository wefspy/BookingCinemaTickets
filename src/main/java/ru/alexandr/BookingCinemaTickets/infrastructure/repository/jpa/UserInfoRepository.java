package ru.alexandr.BookingCinemaTickets.infrastructure.repository.jpa;

import org.springframework.data.repository.CrudRepository;
import ru.alexandr.BookingCinemaTickets.domain.model.UserInfo;

import java.util.Optional;

public interface UserInfoRepository extends CrudRepository<UserInfo, Long> {
    Optional<UserInfo> findByEmail(String email);
}
