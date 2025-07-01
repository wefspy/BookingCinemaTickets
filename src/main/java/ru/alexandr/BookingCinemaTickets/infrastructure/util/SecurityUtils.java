package ru.alexandr.BookingCinemaTickets.infrastructure.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.alexandr.BookingCinemaTickets.infrastructure.security.UserDetailsImpl;

import java.util.Optional;

public final class SecurityUtils {
    private SecurityUtils() {}

    public static Optional<UserDetailsImpl> getCurrentUser() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .filter(Authentication::isAuthenticated)
                .map(Authentication::getPrincipal)
                .filter(UserDetailsImpl.class::isInstance)
                .map(UserDetailsImpl.class::cast);
    }
}
