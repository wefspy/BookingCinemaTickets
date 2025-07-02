package ru.alexandr.BookingCinemaTickets.controller.web;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.alexandr.BookingCinemaTickets.application.dto.session.SessionCreateDto;
import ru.alexandr.BookingCinemaTickets.application.dto.session.SessionUpdateDto;
import ru.alexandr.BookingCinemaTickets.application.dto.sessionSeat.SessionSeatCreateDto;
import ru.alexandr.BookingCinemaTickets.application.dto.sessionSeat.SessionSeatUpdateDto;
import ru.alexandr.BookingCinemaTickets.application.service.HallService;
import ru.alexandr.BookingCinemaTickets.application.service.MovieService;
import ru.alexandr.BookingCinemaTickets.application.service.SessionSeatService;
import ru.alexandr.BookingCinemaTickets.application.service.SessionService;
import ru.alexandr.BookingCinemaTickets.domain.enums.SessionSeatStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/sessions")
public class SessionController {
    private final SessionSeatService sessionSeatService;
    private final SessionService sessionService;
    private final MovieService movieService;
    private final HallService hallService;

    public SessionController(SessionSeatService sessionSeatService,
                             SessionService sessionService,
                             MovieService movieService,
                             HallService hallService) {
        this.sessionSeatService = sessionSeatService;
        this.sessionService = sessionService;
        this.movieService = movieService;
        this.hallService = hallService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/create")
    public String getCreateSessionPage(Model model) {
        model.addAttribute("movies", movieService.getAll(org.springframework.data.domain.PageRequest.of(0, 100)).getContent());
        model.addAttribute("halls", hallService.getAllPreview(org.springframework.data.domain.PageRequest.of(0, 100)).getContent());
        return "sessions/create";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public String createSession(@RequestParam Long movieId,
                                @RequestParam Long hallId,
                                @RequestParam String startTime,
                                @RequestParam BigDecimal priceStandard,
                                @RequestParam BigDecimal priceVip,
                                @RequestParam BigDecimal pricePremium) {
        var hall = hallService.get(hallId);
        List<SessionSeatCreateDto> seats = hall.seats().stream()
                .map(seat -> new SessionSeatCreateDto(
                        seat.id(),
                        seat.type() == ru.alexandr.BookingCinemaTickets.domain.enums.SeatType.VIP ? priceVip :
                        seat.type() == ru.alexandr.BookingCinemaTickets.domain.enums.SeatType.PREMIUM ? pricePremium : priceStandard,
                        SessionSeatStatus.AVAILABLE))
                .toList();
        var sessionCreateDto = new SessionCreateDto(
                movieId,
                hallId,
                LocalDateTime.parse(startTime),
                seats
        );
        sessionService.create(sessionCreateDto);
        return "redirect:/sessions";
    }

    @GetMapping
    public String listSessions(@RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "20") int size,
                              Model model) {
        var sessions = sessionService.getAllByPage(org.springframework.data.domain.PageRequest.of(page, size));
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().contains("ADMIN"));
        boolean isUser = !isAdmin && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().contains("USER"));
        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("isUser", isUser);
        model.addAttribute("sessions", sessions.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", sessions.getTotalPages());
        return "sessions/list";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/edit/{id}")
    public String getEditSessionPage(@PathVariable Long id, Model model) {
        var cinemaSession = sessionService.getById(id);
        model.addAttribute("cinemaSession", cinemaSession);
        model.addAttribute("movies", movieService.getAll(org.springframework.data.domain.PageRequest.of(0, 100)).getContent());
        model.addAttribute("halls", hallService.getAllPreview(org.springframework.data.domain.PageRequest.of(0, 100)).getContent());
        return "sessions/edit";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/edit/{id}")
    public String editSession(@PathVariable Long id,
                              @RequestParam Long movieId,
                              @RequestParam Long hallId,
                              @RequestParam String startTime) {
        var updateDto = new ru.alexandr.BookingCinemaTickets.application.dto.session.SessionUpdateDto(movieId, hallId, java.time.LocalDateTime.parse(startTime));
        sessionService.update(id, updateDto);
        return "redirect:/sessions";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/delete/{id}")
    public String deleteSession(@PathVariable Long id) {
        sessionService.delete(id);
        return "redirect:/sessions";
    }

    @GetMapping("/{sessionId}/seats")
    public String showSessionSeats(@PathVariable Long sessionId, Model model) {
        var session = sessionService.getById(sessionId);
        System.out.println("Session seats count: " + session.seats().size());
        session.seats().forEach(s -> System.out.println(
            "SeatId: " + s.id() + ", row: " + (s.seat() != null ? s.seat().rowNumber() : "null") +
            ", number: " + (s.seat() != null ? s.seat().seatNumber() : "null")
        ));
        model.addAttribute("cinemaSession", session);
        int maxRow = session.seats().stream()
            .mapToInt(seat -> seat.seat().rowNumber())
            .max()
            .orElse(1);
        model.addAttribute("maxRow", maxRow);
        return "sessions/seats";
    }

    @PostMapping("/{sessionId}/seats/book")
    public String bookSeat(@PathVariable Long sessionId, @RequestParam Long sessionSeatId, Authentication authentication, RedirectAttributes redirectAttributes) {
        String username = authentication.getName();
        try {
            sessionSeatService.book(sessionSeatId);
            redirectAttributes.addFlashAttribute("success", "Билет успешно забронирован!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка бронирования: " + e.getMessage());
        }
        return "redirect:/users/profile";
    }
} 