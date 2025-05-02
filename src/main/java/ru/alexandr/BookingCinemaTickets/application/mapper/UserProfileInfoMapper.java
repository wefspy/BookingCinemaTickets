package ru.alexandr.BookingCinemaTickets.application.mapper;

import org.springframework.stereotype.Component;
import ru.alexandr.BookingCinemaTickets.application.dto.UserProfileInfoDto;
import ru.alexandr.BookingCinemaTickets.domain.model.Role;
import ru.alexandr.BookingCinemaTickets.domain.model.User;
import ru.alexandr.BookingCinemaTickets.domain.model.UserInfo;
import ru.alexandr.BookingCinemaTickets.infrastructure.config.DateTimeConfig;

import java.util.Set;

@Component
public class UserProfileInfoMapper {
    private final DateTimeConfig config;
    private final RoleMapper roleMapper;

    public UserProfileInfoMapper(DateTimeConfig config,
                                 RoleMapper roleMapper) {
        this.config = config;
        this.roleMapper = roleMapper;
    }

    public UserProfileInfoDto toDto(
            User user,
            UserInfo userInfo,
            Set<Role> roles
    ) {
        return new UserProfileInfoDto(
                user.getId(),
                user.getUsername(),
                roleMapper.getRoleDtos(roles),
                userInfo.getEmail(),
                userInfo.getPhoneNumber(),
                userInfo.getCreatedAt().format(config.getFormatter())
        );
    }
}
