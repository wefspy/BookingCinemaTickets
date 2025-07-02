package ru.alexandr.BookingCinemaTickets.controller.web;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.alexandr.BookingCinemaTickets.application.dto.hall.HallCreateDto;
import ru.alexandr.BookingCinemaTickets.application.dto.hall.HallUpdateDto;
import ru.alexandr.BookingCinemaTickets.application.dto.seat.SeatCreateDto;
import ru.alexandr.BookingCinemaTickets.application.service.HallService;
import ru.alexandr.BookingCinemaTickets.domain.enums.SeatType;
import ru.alexandr.BookingCinemaTickets.domain.enums.SoundSystem;

import java.util.Arrays;

@Controller
@RequestMapping("/halls")
@PreAuthorize("hasRole('ADMIN')")
public class HallController {
    private final HallService hallService;

    public HallController(HallService hallService) {
        this.hallService = hallService;
    }

    @GetMapping("/create")
    public String getCreateHallPage(Model model) {
        model.addAttribute("hallCreateDto", new HallCreateDto("", SoundSystem.DOLBY_ATMOS, null));
        model.addAttribute("soundSystems", Arrays.asList(SoundSystem.values()));
        model.addAttribute("seatTypes", Arrays.asList(SeatType.values()));
        return "halls/create";
    }

    @PostMapping("/create")
    public String createHall(@ModelAttribute HallCreateDto hallCreateDto, Model model) {
        try {
            hallService.create(hallCreateDto);
            return "redirect:/halls";
        } catch (Exception e) {
            model.addAttribute("error", "Ошибка при создании зала: " + e.getMessage());
            model.addAttribute("soundSystems", Arrays.asList(SoundSystem.values()));
            model.addAttribute("seatTypes", Arrays.asList(SeatType.values()));
            return "halls/create";
        }
    }

    @GetMapping("")
    public String listHalls(Model model, org.springframework.data.domain.Pageable pageable) {
        var hallsPage = hallService.getAllPreview(pageable);
        model.addAttribute("hallsPage", hallsPage);
        return "halls/list";
    }

    @GetMapping("/edit/{id}")
    public String getEditHallPage(@PathVariable Long id, Model model) {
        var hall = hallService.get(id);
        model.addAttribute("hall", hall);
        model.addAttribute("soundSystems", Arrays.asList(SoundSystem.values()));
        model.addAttribute("seatTypes", Arrays.asList(SeatType.values()));
        return "halls/edit";
    }

    @PostMapping("/edit/{id}")
    public String editHall(@PathVariable Long id, @ModelAttribute HallUpdateDto hallUpdateDto, Model model) {
        try {
            hallService.update(id, hallUpdateDto);
            return "redirect:/halls";
        } catch (Exception e) {
            model.addAttribute("error", "Ошибка при редактировании зала: " + e.getMessage());
            model.addAttribute("soundSystems", Arrays.asList(SoundSystem.values()));
            model.addAttribute("seatTypes", Arrays.asList(SeatType.values()));
            return "halls/edit";
        }
    }

    @PostMapping("/delete/{id}")
    public String deleteHall(@PathVariable Long id) {
        hallService.delete(id);
        return "redirect:/halls";
    }
} 