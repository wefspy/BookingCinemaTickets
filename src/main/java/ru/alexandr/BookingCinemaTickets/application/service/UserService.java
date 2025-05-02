package ru.alexandr.BookingCinemaTickets.application.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.alexandr.BookingCinemaTickets.application.dto.UserProfileInfoDto;
import ru.alexandr.BookingCinemaTickets.application.exception.UserNotFoundException;
import ru.alexandr.BookingCinemaTickets.application.mapper.UserProfileInfoMapper;
import ru.alexandr.BookingCinemaTickets.domain.model.Role;
import ru.alexandr.BookingCinemaTickets.domain.model.RoleUser;
import ru.alexandr.BookingCinemaTickets.domain.model.User;
import ru.alexandr.BookingCinemaTickets.infrastructure.repository.jpa.UserInfoRepository;
import ru.alexandr.BookingCinemaTickets.infrastructure.repository.jpa.UserRepository;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserProfileInfoMapper userProfileInfoMapper;

    public UserService(UserInfoRepository userInfoRepository,
                       UserRepository userRepository,
                       UserProfileInfoMapper userProfileInfoMapper) {
        this.userRepository = userRepository;
        this.userProfileInfoMapper = userProfileInfoMapper;
    }

    public UserProfileInfoDto getUserProfileInfo(Long userId) {
        User user = userRepository.findByIdWithInfoAndRoles(userId)
                .orElseThrow(() -> new UserNotFoundException(
                        String.format("Пользователь с id %s не найден", userId)
                ));

        Set<Role> roles = user.getRoleUser().stream()
                .map(RoleUser::getRole)
                .collect(Collectors.toSet());

        return userProfileInfoMapper.toDto(
                user,
                user.getUserInfo(),
                roles
        );
    }

    public Page<UserProfileInfoDto> getUserProfileInfoPage(Pageable pageable) {
        Page<User> userPage = userRepository.findAllWithInfoAndRoles(pageable);

        return userPage.map(user -> userProfileInfoMapper.toDto(
                user,
                user.getUserInfo(),
                user.getRoleUser().stream()
                        .map(RoleUser::getRole)
                        .collect(Collectors.toSet())
        ));
    }
}
