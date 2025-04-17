package ru.alexandr.BookingCinemaTickets.mapper;

import org.springframework.stereotype.Component;
import ru.alexandr.BookingCinemaTickets.config.DateTimeConfig;
import ru.alexandr.BookingCinemaTickets.domain.Role;
import ru.alexandr.BookingCinemaTickets.domain.User;
import ru.alexandr.BookingCinemaTickets.domain.UserInfo;
import ru.alexandr.BookingCinemaTickets.dto.UserProfileInfoDto;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserProfileInfoMapper {
    private final DateTimeConfig config;

    public UserProfileInfoMapper(DateTimeConfig config) {
        this.config = config;
    }

    public UserProfileInfoDto toDto(
            User user,
            UserInfo userInfo,
            Set<Role> roles
    ) {
        Set<String> roleStrings = roles.stream()
                .map(Role::getName)
                .collect(Collectors.toSet());

        return new UserProfileInfoDto(
                user.getUsername(),
                roleStrings,
                userInfo.getEmail(),
                userInfo.getPhoneNumber(),
                userInfo.getCreatedAt().format(config.getFormatter())
        );
    }
}
