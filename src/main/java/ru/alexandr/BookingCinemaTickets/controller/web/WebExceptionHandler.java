package ru.alexandr.BookingCinemaTickets.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.alexandr.BookingCinemaTickets.application.exception.UsernameAlreadyTakenException;

@ControllerAdvice(annotations = Controller.class)
public class WebExceptionHandler {

    @ExceptionHandler(Exception.class)
    public void exception(Exception exception, Model model) {
        model.addAttribute("error", "Произошла ошибка. Пожалуйста, попробуйте позже");
    }

    @ExceptionHandler(UsernameAlreadyTakenException.class)
    public void exception(UsernameAlreadyTakenException exception, Model model) {
        model.addAttribute("error", exception.getMessage());
    }
}
