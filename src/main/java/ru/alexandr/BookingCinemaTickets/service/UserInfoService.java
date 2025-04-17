package ru.alexandr.BookingCinemaTickets.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.alexandr.BookingCinemaTickets.domain.Role;
import ru.alexandr.BookingCinemaTickets.domain.RoleUser;
import ru.alexandr.BookingCinemaTickets.domain.User;
import ru.alexandr.BookingCinemaTickets.domain.UserInfo;
import ru.alexandr.BookingCinemaTickets.dto.UserProfileInfoDto;
import ru.alexandr.BookingCinemaTickets.exception.UserNotFoundException;
import ru.alexandr.BookingCinemaTickets.mapper.UserProfileInfoMapper;
import ru.alexandr.BookingCinemaTickets.repository.UserInfoRepository;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserInfoService {
    private final UserInfoRepository userInfoRepository;
    private final UserProfileInfoMapper userProfileInfoMapper;

    public UserInfoService(UserInfoRepository userInfoRepository,
                           UserProfileInfoMapper userProfileInfoMapper) {
        this.userInfoRepository = userInfoRepository;
        this.userProfileInfoMapper = userProfileInfoMapper;
    }

    @Transactional
    public UserProfileInfoDto getUserProfileInfo(Long userId) {
        Optional<UserInfo> userInfoOptional = userInfoRepository.findById(userId);

        if (userInfoOptional.isEmpty()) {
            throw new UserNotFoundException(String.format("Пользователь с id %s не найден", userId));
        }

        UserInfo userInfo = userInfoOptional.get();
        User user = userInfo.getUser();
        Set<Role> roles = user.getRoleUser().stream()
                .map(RoleUser::getRole)
                .collect(Collectors.toSet());

        return userProfileInfoMapper.toDto(
                user,
                userInfo,
                roles
        );
    }
}
