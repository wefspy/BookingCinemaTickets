package ru.alexandr.BookingCinemaTickets.mapper;

import org.springframework.stereotype.Component;
import ru.alexandr.BookingCinemaTickets.config.DateTimeConfig;
import ru.alexandr.BookingCinemaTickets.domain.Role;
import ru.alexandr.BookingCinemaTickets.domain.User;
import ru.alexandr.BookingCinemaTickets.domain.UserInfo;
import ru.alexandr.BookingCinemaTickets.dto.UserProfileInfoDto;

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
