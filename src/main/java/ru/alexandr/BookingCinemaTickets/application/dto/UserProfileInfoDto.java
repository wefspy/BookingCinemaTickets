package ru.alexandr.BookingCinemaTickets.application.dto;

import java.util.Set;

public record UserProfileInfoDto(
        Long userId,
        String userName,
        Set<RoleDto> roles,
        String email,
        String phoneNumber,
        String createdAt
) {

}
