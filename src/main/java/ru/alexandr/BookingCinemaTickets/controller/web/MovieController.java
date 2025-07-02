package ru.alexandr.BookingCinemaTickets.controller.web;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.alexandr.BookingCinemaTickets.application.dto.movie.MovieCreateDto;
import ru.alexandr.BookingCinemaTickets.application.dto.movie.MovieDto;
import ru.alexandr.BookingCinemaTickets.application.dto.genre.GenreDto;
import ru.alexandr.BookingCinemaTickets.application.service.GenreService;
import ru.alexandr.BookingCinemaTickets.application.service.MovieService;
import ru.alexandr.BookingCinemaTickets.domain.enums.Rating;

@Controller
@RequestMapping("/movies")
public class MovieController {
    private final MovieService movieService;
    private final GenreService genreService;
    private static final int GENRES_PAGE_SIZE = 100;

    public MovieController(MovieService movieService, GenreService genreService) {
        this.movieService = movieService;
        this.genreService = genreService;
    }

    @GetMapping
    public String getMoviesPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model
    ) {
        Page<MovieDto> movies = movieService.getAll(PageRequest.of(page, size));
        Page<GenreDto> genresPage = genreService.getAll(PageRequest.of(0, GENRES_PAGE_SIZE));
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().contains("ADMIN"));
        boolean isUser = !isAdmin && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().contains("USER"));
        model.addAttribute("isUser", isUser);
        model.addAttribute("isAdmin", isAdmin);
        
        model.addAttribute("movies", movies);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", movies.getTotalPages());
        model.addAttribute("genres", genresPage.getContent());
        model.addAttribute("ratings", Rating.values());
        return "movies/list";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/create")
    public String getCreateMoviePage(Model model) {
        Page<GenreDto> genresPage = genreService.getAll(PageRequest.of(0, GENRES_PAGE_SIZE));
        model.addAttribute("genres", genresPage.getContent());
        model.addAttribute("ratings", Rating.values());
        return "movies/create";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    @ResponseBody
    public ResponseEntity<?> createMovie(@ModelAttribute MovieCreateDto movieCreateDto) {
        try {
            MovieDto createdMovie = movieService.create(movieCreateDto);
            return ResponseEntity.ok(createdMovie);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Ошибка при создании фильма: " + e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/edit/{id}")
    public String getEditMoviePage(@PathVariable Long id, Model model) {
        MovieDto movie = movieService.getById(id);
        Page<GenreDto> genresPage = genreService.getAll(PageRequest.of(0, GENRES_PAGE_SIZE));
        model.addAttribute("movie", movie);
        model.addAttribute("genres", genresPage.getContent());
        model.addAttribute("ratings", Rating.values());
        return "movies/edit";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/edit/{id}")
    @ResponseBody
    public ResponseEntity<?> updateMovie(@PathVariable Long id, @ModelAttribute MovieCreateDto movieCreateDto) {
        try {
            MovieDto updatedMovie = movieService.update(id, movieCreateDto);
            return ResponseEntity.ok(updatedMovie);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Ошибка при редактировании фильма: " + e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/delete/{id}")
    public String deleteMovie(@PathVariable Long id) {
        movieService.delete(id);
        return "redirect:/movies";
    }
} 