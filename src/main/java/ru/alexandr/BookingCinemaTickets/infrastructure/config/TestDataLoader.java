package ru.alexandr.BookingCinemaTickets.infrastructure.config;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import ru.alexandr.BookingCinemaTickets.application.dto.RegisterDto;
import ru.alexandr.BookingCinemaTickets.application.dto.UserProfileInfoDto;
import ru.alexandr.BookingCinemaTickets.application.exception.RoleNotFoundException;
import ru.alexandr.BookingCinemaTickets.application.service.RegistrationService;
import ru.alexandr.BookingCinemaTickets.application.service.UserService;
import ru.alexandr.BookingCinemaTickets.domain.model.Role;
import ru.alexandr.BookingCinemaTickets.infrastructure.repository.jpa.RoleRepository;
import ru.alexandr.BookingCinemaTickets.infrastructure.repository.jpa.UserRepository;
import ru.alexandr.BookingCinemaTickets.infrastructure.security.RoleEnum;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;


// TODO: Заменить на liquibase
@Profile("!test")
@Configuration
public class TestDataLoader implements ApplicationRunner {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final RegistrationService registrationService;
    private final UserService userService;

    public TestDataLoader(RoleRepository roleRepository,
                          UserRepository userRepository,
                          RegistrationService registrationService,
                          UserService userService) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.registrationService = registrationService;
        this.userService = userService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        seedRoles();
        seedUsers();
    }

    private void seedRoles() {
        Set<String> roles = Arrays.stream(RoleEnum.values())
                .map(RoleEnum::getAuthority)
                .collect(Collectors.toSet());

        Collection<String> existsRoles = roleRepository.findByNameIn(roles).stream()
                .map(Role::getName)
                .toList();
        Collection<Role> notExistsRoles = roles.stream()
                .filter(r -> !existsRoles.contains(r))
                .map(Role::new)
                .toList();

        roleRepository.saveAll(notExistsRoles);
    }

    private void seedUsers() {
        String username = "admin";

        if (userRepository.existsByUsername(username)) {
            return;
        }

        RegisterDto dto = new RegisterDto(
                username,
                "1",
                null,
                null
        );

        UserProfileInfoDto userProfile = registrationService.register(dto);

        Long roleId = roleRepository.findByName(RoleEnum.ADMIN.getAuthority())
                .orElseThrow(() -> new RoleNotFoundException(RoleEnum.ADMIN.getAuthority()))
                .getId();
        userService.assignRoleToUser(userProfile.userId(), roleId);
    }
}
