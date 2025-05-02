package ru.alexandr.BookingCinemaTickets.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.alexandr.BookingCinemaTickets.application.dto.UserProfileInfoDto;
import ru.alexandr.BookingCinemaTickets.application.dto.UserRegisterDto;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final RoleUserRepository roleUserRepository;
    private final UserProfileInfoMapper userProfileInfoMapper;

    public AuthService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       RoleUserRepository roleUserRepository,
                       UserProfileInfoMapper userProfileInfoMapper) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.roleUserRepository = roleUserRepository;
        this.userProfileInfoMapper = userProfileInfoMapper;
    }

    @Transactional
    public UserProfileInfoDto createUserWithInfo(UserRegisterDto userRegisterDto) {
        if (userRepository.existsByUsername(userRegisterDto.username())) {
            throw new UsernameAlreadyTakenException(
                    String.format("Имя пользователя %s уже занято", userRegisterDto.username())
            );
        }

        User user = new User(
                userRegisterDto.username(),
                userRegisterDto.password()
        );

        UserInfo userInfo = new UserInfo(
                user,
                userRegisterDto.createdAt() == null ? LocalDateTime.now() : userRegisterDto.createdAt()
        );
        if (userRegisterDto.email() != null) {
            userInfo.setEmail(userRegisterDto.email());
        }
        if (userRegisterDto.phoneNumber() != null) {
            userInfo.setPhoneNumber(userRegisterDto.phoneNumber());
        }

        userRepository.save(user);

        Set<Role> roles = StreamSupport
                .stream(roleRepository.findAllById(userRegisterDto.roleIds()).spliterator(), false)
                .collect(Collectors.toSet());

        if (roles.size() != userRegisterDto.roleIds().size()) {
            Set<Long> foundIds = roles.stream()
                    .map(Role::getId)
                    .collect(Collectors.toSet());

            List<Long> notFoundIds = userRegisterDto.roleIds().stream()
                    .filter(id -> !foundIds.contains(id))
                    .toList();

            throw new RoleNotFoundException(
                    String.format("Роли с id %s не найдены", notFoundIds)
            );
        }

        List<RoleUser> roleUsers = roles.stream()
                .map(role -> new RoleUser(user, role))
                .toList();

        roleUserRepository.saveAll(roleUsers);

        return userProfileInfoMapper.toDto(
                user,
                userInfo,
                roles
        );
    }
}
