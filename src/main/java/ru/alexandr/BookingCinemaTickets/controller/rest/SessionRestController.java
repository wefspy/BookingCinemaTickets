package ru.alexandr.BookingCinemaTickets.controller.rest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.alexandr.BookingCinemaTickets.application.dto.session.SessionCreateDto;
import ru.alexandr.BookingCinemaTickets.application.dto.session.SessionDto;
import ru.alexandr.BookingCinemaTickets.application.dto.session.SessionPreviewDto;
import ru.alexandr.BookingCinemaTickets.application.dto.session.SessionUpdateDto;
import ru.alexandr.BookingCinemaTickets.application.service.SessionService;

@RestController
@RequestMapping("/api")
public class SessionRestController {
    private final SessionService sessionService;

    public SessionRestController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @PostMapping("/sessions")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SessionDto> create(@RequestBody SessionCreateDto sessionCreateDto) {
        SessionDto responseBody = sessionService.create(sessionCreateDto);
        return ResponseEntity.status(201).body(responseBody);
    }


    @GetMapping("/halls/{hallId}/sessions")
    public ResponseEntity<Page<SessionPreviewDto>> getAllByHallId(@PathVariable Long hallId, Pageable pageable) {
        Page<SessionPreviewDto> responseBody = sessionService.getAllByHallId(hallId, pageable);
        return ResponseEntity.status(200).body(responseBody);
    }

    @GetMapping("/movies/{movieId}/sessions")
    public ResponseEntity<Page<SessionPreviewDto>> getAllByMovieId(@PathVariable Long movieId, Pageable pageable) {
        Page<SessionPreviewDto> responseBody = sessionService.getAllByMovieId(movieId, pageable);
        return ResponseEntity.status(200).body(responseBody);
    }

    @GetMapping("/sessions/{id}")
    public ResponseEntity<SessionDto> getById(@PathVariable Long id) {
        SessionDto responseBody = sessionService.getById(id);
        return ResponseEntity.status(200).body(responseBody);
    }

    @PutMapping("/sessions/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SessionPreviewDto> update(@PathVariable Long id, @RequestBody SessionUpdateDto sessionUpdateDto) {
        SessionPreviewDto responseBody = sessionService.update(id, sessionUpdateDto);
        return ResponseEntity.status(200).body(responseBody);
    }

    @DeleteMapping("/sessions/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        sessionService.delete(id);
        return ResponseEntity.status(200).build();
    }
}
