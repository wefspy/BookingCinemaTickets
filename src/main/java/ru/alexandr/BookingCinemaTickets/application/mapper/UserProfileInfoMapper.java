package ru.alexandr.BookingCinemaTickets.application.mapper;

import org.springframework.stereotype.Component;
import ru.alexandr.BookingCinemaTickets.application.dto.UserProfileInfoDto;
import ru.alexandr.BookingCinemaTickets.domain.model.Role;
import ru.alexandr.BookingCinemaTickets.domain.model.RoleUser;
import ru.alexandr.BookingCinemaTickets.domain.model.User;
import ru.alexandr.BookingCinemaTickets.domain.model.UserInfo;

import java.util.Collection;
import java.util.stream.Collectors;

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
                roleMapper.toDtos(roles),
                userInfo.getEmail(),
                userInfo.getPhoneNumber(),
                userInfo.getCreatedAt()
        );
    }

    public UserProfileInfoDto toDto(User userWithInfoAndRoles) {
        UserInfo userInfo = userWithInfoAndRoles.getUserInfo();
        Collection<Role> roles = userWithInfoAndRoles.getRoleUser().stream()
                .map(RoleUser::getRole)
                .collect(Collectors.toSet());

        return toDto(userWithInfoAndRoles, userInfo, roles);
    }
}
