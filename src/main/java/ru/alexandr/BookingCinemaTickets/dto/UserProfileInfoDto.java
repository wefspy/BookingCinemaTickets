package ru.alexandr.BookingCinemaTickets.dto;

import java.util.Set;

public record UserProfileInfoDto(
        String userName,
        Set<String> roles,
        String email,
        String phoneNumber,
        String createdAt
) {

}
