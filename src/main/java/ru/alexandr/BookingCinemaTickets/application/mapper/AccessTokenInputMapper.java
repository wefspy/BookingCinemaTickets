package ru.alexandr.BookingCinemaTickets.application.mapper;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import ru.alexandr.BookingCinemaTickets.domain.model.User;
import ru.alexandr.BookingCinemaTickets.infrastructure.security.UserDetailsImpl;
import ru.alexandr.BookingCinemaTickets.infrastructure.security.jwt.dto.AccessTokenInput;

import java.util.stream.Collectors;

@Component
public class AccessTokenInputMapper {
    public AccessTokenInput from(UserDetailsImpl user) {
        return new AccessTokenInput(
                user.getId(),
                user.getUsername(),
                user.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toSet())
        );
    }

    public AccessTokenInput from(User userWithRoles) {
        return new AccessTokenInput(
                userWithRoles.getId(),
                userWithRoles.getUsername(),
                userWithRoles.getRoleUser().stream()
                        .map(ru -> ru.getRole().getName())
                        .collect(Collectors.toSet())
        );
    }
}
