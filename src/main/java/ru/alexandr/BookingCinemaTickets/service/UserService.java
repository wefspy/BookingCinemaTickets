package ru.alexandr.BookingCinemaTickets.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.alexandr.BookingCinemaTickets.domain.Role;
import ru.alexandr.BookingCinemaTickets.domain.RoleUser;
import ru.alexandr.BookingCinemaTickets.domain.User;
import ru.alexandr.BookingCinemaTickets.dto.UserProfileInfoDto;
import ru.alexandr.BookingCinemaTickets.exception.UserNotFoundException;
import ru.alexandr.BookingCinemaTickets.mapper.UserProfileInfoMapper;
import ru.alexandr.BookingCinemaTickets.repository.UserInfoRepository;
import ru.alexandr.BookingCinemaTickets.repository.UserRepository;

import java.util.Optional;
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
        Optional<User> userInfoOptional = userRepository.findByIdWithInfoAndRoles(userId);

        if (userInfoOptional.isEmpty()) {
            throw new UserNotFoundException(String.format("Пользователь с id %s не найден", userId));
        }

        User user = userInfoOptional.get();
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
                user.getRoleUser()
                        .stream()
                        .map(RoleUser::getRole)
                        .collect(Collectors.toSet())
        ));
    }
}
