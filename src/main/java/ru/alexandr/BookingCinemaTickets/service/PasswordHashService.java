package ru.alexandr.BookingCinemaTickets.service;

import org.mindrot.jbcrypt.BCrypt;

public final class PasswordHashService {
    private PasswordHashService() {}

    public static String getHash(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public static boolean checkPassword(String password, String hashedPassword) {
        return BCrypt.checkpw(password, hashedPassword);
    }
}
