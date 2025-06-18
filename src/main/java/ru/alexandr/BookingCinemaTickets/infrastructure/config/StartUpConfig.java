package ru.alexandr.BookingCinemaTickets.infrastructure.config;

import jakarta.transaction.Transactional;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import ru.alexandr.BookingCinemaTickets.application.dto.RegisterDto;
import ru.alexandr.BookingCinemaTickets.application.dto.UserProfileInfoDto;
import ru.alexandr.BookingCinemaTickets.application.exception.RoleNotFoundException;
import ru.alexandr.BookingCinemaTickets.application.service.RegistrationService;
import ru.alexandr.BookingCinemaTickets.application.service.UserService;
import ru.alexandr.BookingCinemaTickets.infrastructure.config.property.RootProperties;
import ru.alexandr.BookingCinemaTickets.infrastructure.repository.jpa.RoleRepository;
import ru.alexandr.BookingCinemaTickets.infrastructure.repository.jpa.UserRepository;
import ru.alexandr.BookingCinemaTickets.infrastructure.security.RoleEnum;


@Configuration
public class StartUpConfig implements ApplicationRunner {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final RegistrationService registrationService;
    private final UserService userService;
    private final RootProperties rootProperties;

    public StartUpConfig(RoleRepository roleRepository,
                         UserRepository userRepository,
                         RegistrationService registrationService,
                         UserService userService,
                         RootProperties rootProperties) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.registrationService = registrationService;
        this.userService = userService;
        this.rootProperties = rootProperties;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        seedRoot();
    }


    protected void seedRoot() {
        if (userRepository.existsByUsername(rootProperties.getUsername())) {
            return;
        }

        RegisterDto dto = new RegisterDto(
                rootProperties.getUsername(),
                rootProperties.getPassword(),
                rootProperties.getEmail(),
                null
        );

        UserProfileInfoDto userProfile = registrationService.register(dto);
        assignRole(userProfile.userId(), RoleEnum.ADMIN.name());
    }

    private void assignRole(Long userId, String roleName) {
        Long roleId = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RoleNotFoundException(
                        String.format("Роль с названием %s не найдена", roleName)
                ))
                .getId();

        userService.assignRoleToUser(userId, roleId);
    }
}
