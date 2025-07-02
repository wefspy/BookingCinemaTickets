package ru.alexandr.BookingCinemaTickets.controller.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.alexandr.BookingCinemaTickets.application.dto.sessionSeat.SessionSeatCreateDto;
import ru.alexandr.BookingCinemaTickets.application.dto.sessionSeat.SessionSeatDto;
import ru.alexandr.BookingCinemaTickets.application.dto.sessionSeat.SessionSeatUpdateDto;
import ru.alexandr.BookingCinemaTickets.application.service.SessionSeatService;

import java.util.List;

@RestController
@RequestMapping("/api")
public class SessionSeatRestController {
    private final SessionSeatService sessionSeatService;

    public SessionSeatRestController(SessionSeatService sessionSeatService) {
        this.sessionSeatService = sessionSeatService;
    }

    @PostMapping("/sessions/{sessionId}/seats")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<SessionSeatDto>> create(@PathVariable Long sessionId,
                                                       @RequestBody List<SessionSeatCreateDto> seats) {
        List<SessionSeatDto> responseBody = sessionSeatService.create(sessionId, seats);
        return ResponseEntity.status(201).body(responseBody);
    }

    @PutMapping("/sessions/{sessionId}/seats/{sessionSeatId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SessionSeatDto> update(@PathVariable Long sessionId,
                                                 @PathVariable Long sessionSeatId,
                                                 @RequestBody SessionSeatUpdateDto sessionSeatUpdateDto) {
        SessionSeatDto responseBody = sessionSeatService.update(sessionSeatId, sessionId, sessionSeatUpdateDto);
        return ResponseEntity.status(200).body(responseBody);
    }

    @DeleteMapping("/sessions/{sessionId}/seats/{sessionSeatId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long sessionId, @PathVariable Long sessionSeatId) {
        sessionSeatService.delete(sessionId, sessionSeatId);
        return ResponseEntity.status(200).build();
    }
}
