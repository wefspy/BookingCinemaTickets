package ru.alexandr.BookingCinemaTickets.controller.web;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.alexandr.BookingCinemaTickets.application.dto.UserProfileInfoDto;
import ru.alexandr.BookingCinemaTickets.application.service.UserService;
import ru.alexandr.BookingCinemaTickets.infrastructure.util.SecurityUtils;

@Controller
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String userListView(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {
        Pageable pageable = PageRequest.of(page, size);
        Page<UserProfileInfoDto> userPage = userService.getUserProfileInfoPage(pageable);

        model.addAttribute("page", userPage);
        model.addAttribute("users", userPage.getContent());

        return "users";
    }

    @GetMapping("/profile")
    public String userProfile(Model model) {
        var userOpt = SecurityUtils.getCurrentUser();
        if (userOpt.isEmpty()) {
            return "redirect:/login";
        }
        var userId = userOpt.get().getId();
        var profile = userService.getUserProfileInfo(userId);
        model.addAttribute("profile", profile);
        return "profile";
    }
}
