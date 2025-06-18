package ru.alexandr.BookingCinemaTickets.application.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.alexandr.BookingCinemaTickets.application.dto.RegisterDto;
import ru.alexandr.BookingCinemaTickets.application.dto.UserProfileInfoDto;
import ru.alexandr.BookingCinemaTickets.application.exception.RoleNotFoundException;
import ru.alexandr.BookingCinemaTickets.application.exception.UsernameAlreadyTakenException;
import ru.alexandr.BookingCinemaTickets.application.mapper.UserProfileInfoMapper;
import ru.alexandr.BookingCinemaTickets.domain.model.Role;
import ru.alexandr.BookingCinemaTickets.domain.model.RoleUser;
import ru.alexandr.BookingCinemaTickets.domain.model.User;
import ru.alexandr.BookingCinemaTickets.domain.model.UserInfo;
import ru.alexandr.BookingCinemaTickets.infrastructure.repository.jpa.RoleRepository;
import ru.alexandr.BookingCinemaTickets.infrastructure.repository.jpa.RoleUserRepository;
import ru.alexandr.BookingCinemaTickets.infrastructure.repository.jpa.UserRepository;
import ru.alexandr.BookingCinemaTickets.infrastructure.security.RoleEnum;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RegistrationService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final RoleUserRepository roleUserRepository;
    private final UserProfileInfoMapper userProfileInfoMapper;
    private final PasswordEncoder passwordEncoder;

    public RegistrationService(UserRepository userRepository,
                               RoleRepository roleRepository,
                               RoleUserRepository roleUserRepository,
                               UserProfileInfoMapper userProfileInfoMapper,
                               PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.roleUserRepository = roleUserRepository;
        this.userProfileInfoMapper = userProfileInfoMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserProfileInfoDto register(RegisterDto dto) {
        validateUsername(dto.username());

        User user = createUser(dto);
        UserInfo userInfo = createUserInfo(user, dto);
        Collection<Role> roles = fetchBasicRolesWithRoleUser();
        assignRolesToUser(user, roles);

        return userProfileInfoMapper.toDto(user, userInfo, roles);
    }

    private void validateUsername(String username) {
        if (userRepository.existsByUsername(username)) {
            throw new UsernameAlreadyTakenException("Имя пользователя %s уже занято".formatted(username));
        }
    }

    private User createUser(RegisterDto dto) {
        User user = new User(dto.username(), passwordEncoder.encode(dto.password()));
        userRepository.save(user);
        return user;
    }

    private UserInfo createUserInfo(User user, RegisterDto dto) {
        UserInfo userInfo = new UserInfo(
                user,
                LocalDateTime.now()
        );
        if (dto.email() != null) {
            userInfo.setEmail(dto.email());
        }
        if (dto.phoneNumber() != null) {
            userInfo.setPhoneNumber(dto.phoneNumber());
        }
        return userInfo;
    }

    private Collection<Role> fetchBasicRolesWithRoleUser() {
        Set<String> basicRoles = Set.of(RoleEnum.USER.name());
        Collection<Role> foundRoles = roleRepository.findAllByNameWithRoleUser(basicRoles);

        if (basicRoles.size() != foundRoles.size()) {
            Set<String> foundNames = foundRoles.stream()
                    .map(Role::getName)
                    .collect(Collectors.toSet());

            List<String> notFoundNames = basicRoles.stream()
                    .filter(name -> !foundNames.contains(name))
                    .toList();

            throw new RoleNotFoundException(
                    String.format("Роли %s не найдены", notFoundNames)
            );
        }

        return foundRoles;
    }

    private void assignRolesToUser(User user, Collection<Role> roles) {
        List<RoleUser> roleUsers = roles.stream()
                .map(role -> new RoleUser(user, role))
                .toList();
        roleUserRepository.saveAll(roleUsers);
    }
}
