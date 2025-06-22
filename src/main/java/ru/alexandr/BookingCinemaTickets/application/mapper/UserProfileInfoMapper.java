package ru.alexandr.BookingCinemaTickets.application.mapper;

import org.springframework.stereotype.Component;
import ru.alexandr.BookingCinemaTickets.application.dto.UserProfileInfoDto;
import ru.alexandr.BookingCinemaTickets.domain.model.Role;
import ru.alexandr.BookingCinemaTickets.domain.model.User;
import ru.alexandr.BookingCinemaTickets.domain.model.UserInfo;

import java.util.Collection;

@Component
public class UserProfileInfoMapper {
    private final RoleMapper roleMapper;

    public UserProfileInfoMapper(RoleMapper roleMapper) {
        this.roleMapper = roleMapper;
    }

    public UserProfileInfoDto toDto(
            User user,
            UserInfo userInfo,
            Collection<Role> roles
    ) {
        return new UserProfileInfoDto(
                user.getId(),
                user.getUsername(),
                roleMapper.getRoleDtos(roles),
                userInfo.getEmail(),
                userInfo.getPhoneNumber(),
                userInfo.getCreatedAt()
        );
    }
}
