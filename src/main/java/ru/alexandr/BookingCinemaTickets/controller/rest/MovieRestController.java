package ru.alexandr.BookingCinemaTickets.controller.rest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.alexandr.BookingCinemaTickets.application.dto.movie.MovieCreateDto;
import ru.alexandr.BookingCinemaTickets.application.dto.movie.MovieDto;
import ru.alexandr.BookingCinemaTickets.application.service.MovieService;

@RestController
@RequestMapping("/api/movies")
public class MovieRestController {
    private final MovieService movieService;

    public MovieRestController(MovieService movieService) {
        this.movieService = movieService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<MovieDto> create(@RequestBody MovieCreateDto request) {
        MovieDto responseBody = movieService.create(request);
        return ResponseEntity.status(201).body(responseBody);
    }

    @GetMapping
    public ResponseEntity<Page<MovieDto>> getAll(Pageable pageable) {
        Page<MovieDto> page = movieService.getAll(pageable);
        return ResponseEntity.status(200).body(page);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<MovieDto> update(@PathVariable Long id, @RequestBody MovieCreateDto request) {
        MovieDto responseBody = movieService.update(id, request);
        return ResponseEntity.status(200).body(responseBody);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        movieService.delete(id);
        return ResponseEntity.status(200).build();
    }
}
