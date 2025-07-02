package ru.alexandr.BookingCinemaTickets.controller.rest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.alexandr.BookingCinemaTickets.application.dto.genre.GenreCreateDto;
import ru.alexandr.BookingCinemaTickets.application.dto.genre.GenreDto;
import ru.alexandr.BookingCinemaTickets.application.service.GenreService;

@RestController
@RequestMapping("/api/genres")
public class GenreRestController {
    private final GenreService genreService;

    public GenreRestController(GenreService genreService) {
        this.genreService = genreService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<GenreDto> create(@RequestBody GenreCreateDto request) {
        GenreDto responseBody = genreService.create(request);
        return ResponseEntity.status(201).body(responseBody);
    }

    @GetMapping
    public ResponseEntity<Page<GenreDto>> getAll(
            @PageableDefault(sort = "name", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        Page<GenreDto> responseBody = genreService.getAll(pageable);
        return ResponseEntity.status(200).body(responseBody);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<GenreDto> update(@PathVariable Long id, @RequestBody GenreCreateDto request) {
        GenreDto responseBody = genreService.update(id, request);
        return ResponseEntity.status(200).body(responseBody);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        genreService.delete(id);
        return ResponseEntity.status(200).build();
    }
}
