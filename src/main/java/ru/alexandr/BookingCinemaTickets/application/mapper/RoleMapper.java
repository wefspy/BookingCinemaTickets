package ru.alexandr.BookingCinemaTickets.application.mapper;

import org.springframework.stereotype.Component;
import ru.alexandr.BookingCinemaTickets.application.dto.RoleDto;
import ru.alexandr.BookingCinemaTickets.domain.model.Role;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class RoleMapper {

    public RoleDto getRoleDto(Role role) {
        return new RoleDto(
                role.getId(),
                role.getName()
        );
    }

    public Set<RoleDto> getRoleDtos(Set<Role> roles) {
        return roles.stream()
                .map(this::getRoleDto)
                .collect(Collectors.toSet());
    }
}
