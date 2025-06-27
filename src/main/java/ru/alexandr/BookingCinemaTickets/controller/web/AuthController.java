package ru.alexandr.BookingCinemaTickets.controller.web;

import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    @GetMapping("/login")
    public String login(HttpSession session) {
        AuthenticationException exception = (AuthenticationException) session
                .getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        if (exception != null) {
            throw exception;
        }
        return "login";
    }
}
