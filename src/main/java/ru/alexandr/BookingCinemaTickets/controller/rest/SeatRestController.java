package ru.alexandr.BookingCinemaTickets.controller.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.alexandr.BookingCinemaTickets.application.dto.seat.SeatCreateDto;
import ru.alexandr.BookingCinemaTickets.application.dto.seat.SeatDto;
import ru.alexandr.BookingCinemaTickets.application.service.SeatService;

@RestController
@RequestMapping("/api/halls/{hallId}/seats")
public class SeatRestController {
    private final SeatService seatService;
    
    public SeatRestController(SeatService seatService) {
        this.seatService = seatService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{seatId}")
    public ResponseEntity<SeatDto> update(@PathVariable Long hallId,
                                          @PathVariable Long seatId,
                                          @RequestBody SeatCreateDto seatCreateDto) {
        SeatDto responseBody = seatService.update(hallId, seatId, seatCreateDto);
        return ResponseEntity.status(200).body(responseBody);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{seatId}")
    public ResponseEntity<Void> delete(@PathVariable Long hallId,
                                       @PathVariable Long seatId) {
        seatService.delete(hallId, seatId);
        return ResponseEntity.status(200).build();
    }
}
