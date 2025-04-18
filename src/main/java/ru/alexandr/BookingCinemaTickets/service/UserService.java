package ru.alexandr.BookingCinemaTickets.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.alexandr.BookingCinemaTickets.domain.Role;
import ru.alexandr.BookingCinemaTickets.domain.RoleUser;
import ru.alexandr.BookingCinemaTickets.domain.User;
import ru.alexandr.BookingCinemaTickets.domain.UserInfo;
import ru.alexandr.BookingCinemaTickets.dto.UserProfileInfoDto;
import ru.alexandr.BookingCinemaTickets.dto.UserRegisterDto;
import ru.alexandr.BookingCinemaTickets.exception.RoleNotFoundException;
import ru.alexandr.BookingCinemaTickets.mapper.UserProfileInfoMapper;
import ru.alexandr.BookingCinemaTickets.repository.RoleRepository;
import ru.alexandr.BookingCinemaTickets.repository.RoleUserRepository;
import ru.alexandr.BookingCinemaTickets.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final RoleUserRepository roleUserRepository;
    private final UserProfileInfoMapper userProfileInfoMapper;

    public UserService(UserRepository userRepository,
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
