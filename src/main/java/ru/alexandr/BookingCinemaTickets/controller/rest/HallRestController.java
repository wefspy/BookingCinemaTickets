package ru.alexandr.BookingCinemaTickets.controller.rest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.alexandr.BookingCinemaTickets.application.dto.hall.HallCreateDto;
import ru.alexandr.BookingCinemaTickets.application.dto.hall.HallDto;
import ru.alexandr.BookingCinemaTickets.application.dto.hall.HallPreviewDto;
import ru.alexandr.BookingCinemaTickets.application.dto.hall.HallUpdateDto;
import ru.alexandr.BookingCinemaTickets.application.service.HallService;
import ru.alexandr.BookingCinemaTickets.application.service.SeatService;

@RestController
@RequestMapping("/api/halls")
public class HallRestController {
    private final HallService hallService;
    private final SeatService seatService;

    public HallRestController(HallService hallService, SeatService seatService) {
        this.hallService = hallService;
        this.seatService = seatService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<HallDto> create(@RequestBody HallCreateDto request) {
        HallDto responseBody = hallService.create(request);
        return ResponseEntity.status(201).body(responseBody);
    }

    @GetMapping
    public ResponseEntity<Page<HallPreviewDto>> getAllPreview(Pageable pageable) {
        Page<HallPreviewDto> responseBody = hallService.getAllPreview(pageable);
        return ResponseEntity.status(200).body(responseBody);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HallDto> get(@PathVariable Long id) {
        HallDto responseBody = hallService.get(id);
        return ResponseEntity.status(200).body(responseBody);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<HallPreviewDto> update(@PathVariable Long id, @RequestBody HallUpdateDto request) {
        HallPreviewDto responseBody = hallService.update(id, request);
        return ResponseEntity.status(200).body(responseBody);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        hallService.delete(id);
    }
}
