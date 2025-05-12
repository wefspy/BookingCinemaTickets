package ru.alexandr.BookingCinemaTickets.application.mapper;

import org.springframework.stereotype.Component;
import ru.alexandr.BookingCinemaTickets.application.dto.RoleDto;
import ru.alexandr.BookingCinemaTickets.domain.model.Role;

import java.util.Collection;
import java.util.stream.Collectors;

@Component
public class RoleMapper {

    public RoleDto getRoleDto(Role role) {
        return new RoleDto(
                role.getId(),
                role.getName()
        );
    }

    public Collection<RoleDto> getRoleDtos(Collection<Role> roles) {
        return roles.stream()
                .map(this::getRoleDto)
                .collect(Collectors.toSet());
    }
}
