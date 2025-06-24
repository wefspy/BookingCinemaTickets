package ru.alexandr.BookingCinemaTickets.controller.rest;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.alexandr.BookingCinemaTickets.application.dto.ApiErrorDto;
import ru.alexandr.BookingCinemaTickets.application.exception.RoleNotFoundException;
import ru.alexandr.BookingCinemaTickets.application.exception.UsernameAlreadyTakenException;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.stream.Collectors;

@RestControllerAdvice(annotations = RestController.class)
public class RestExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorDto> exception(MethodArgumentNotValidException exception,
                                                 HttpServletRequest request) {
        String userMessage = exception.getBindingResult().getAllErrors()
                .stream()
                .map(ObjectError::getDefaultMessage)
                .collect(Collectors.joining(System.lineSeparator()));

        return buildErrorResponse(
                exception,
                userMessage,
                HttpStatus.BAD_REQUEST,
                request
        );
    }

    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<ApiErrorDto> exception(RoleNotFoundException exception,
                                                 HttpServletRequest request) {
        return buildErrorResponse(
                exception,
                exception.getMessage(),
                HttpStatus.NOT_FOUND,
                request
        );
    }

    @ExceptionHandler(UsernameAlreadyTakenException.class)
    public ResponseEntity<ApiErrorDto> exception(UsernameAlreadyTakenException exception,
                                                 HttpServletRequest request) {
        return buildErrorResponse(
                exception,
                exception.getMessage(),
                HttpStatus.CONFLICT,
                request
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorDto> exception(Exception exception,
                                                 HttpServletRequest request) {
        return buildErrorResponse(
                exception,
                "Произошла внутренняя ошибка сервера",
                HttpStatus.INTERNAL_SERVER_ERROR,
                request
        );
    }

    private ResponseEntity<ApiErrorDto> buildErrorResponse(
            Exception exception,
            String userMessage,
            HttpStatus httpStatus,
            HttpServletRequest request
    ) {
        ApiErrorDto apiErrorDto = new ApiErrorDto(
                exception.getClass().getName(),
                exception.getMessage(),
                userMessage,
                httpStatus.value(),
                Arrays.stream(exception.getStackTrace())
                        .map(StackTraceElement::toString)
                        .toList(),
                ZonedDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME),
                request.getRequestURI()
        );

        return ResponseEntity.status(httpStatus)
                .body(apiErrorDto);
    }
}
