package ru.alexandr.BookingCinemaTickets.controller.web;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.alexandr.BookingCinemaTickets.application.dto.genre.GenreCreateDto;
import ru.alexandr.BookingCinemaTickets.application.dto.genre.GenreDto;
import ru.alexandr.BookingCinemaTickets.application.service.GenreService;

@Controller
@RequestMapping("/genres")
@PreAuthorize("hasRole('ADMIN')")
public class GenreController {
    private final GenreService genreService;

    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    @GetMapping
    public String getGenresPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model
    ) {
        Page<GenreDto> genres = genreService.getAll(PageRequest.of(page, size));
        model.addAttribute("genres", genres);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", genres.getTotalPages());
        return "genres/list";
    }

    @PostMapping("/create")
    @ResponseBody
    public ResponseEntity<?> createGenre(@ModelAttribute GenreCreateDto genreCreateDto) {
        try {
            GenreDto createdGenre = genreService.create(genreCreateDto);
            return ResponseEntity.ok(createdGenre);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Ошибка при создании жанра: " + e.getMessage());
        }
    }

    @PostMapping("/edit/{id}")
    @ResponseBody
    public ResponseEntity<?> updateGenre(@PathVariable Long id, @ModelAttribute GenreCreateDto genreCreateDto) {
        try {
            GenreDto updatedGenre = genreService.update(id, genreCreateDto);
            return ResponseEntity.ok(updatedGenre);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Ошибка при редактировании жанра: " + e.getMessage());
        }
    }

    @PostMapping("/delete/{id}")
    @ResponseBody
    public ResponseEntity<?> deleteGenre(@PathVariable Long id) {
        try {
            genreService.delete(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Ошибка при удалении жанра: " + e.getMessage());
        }
    }
} 