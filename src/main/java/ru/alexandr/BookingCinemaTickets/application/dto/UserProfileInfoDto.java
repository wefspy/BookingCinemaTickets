package ru.alexandr.BookingCinemaTickets.application.dto;

import java.util.Collection;

public record UserProfileInfoDto(
        Long userId,
        String userName,
        Collection<RoleDto> roles,
        String email,
        String phoneNumber,
        String createdAt
) {

}
