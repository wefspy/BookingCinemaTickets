package ru.alexandr.BookingCinemaTickets.application.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.alexandr.BookingCinemaTickets.application.dto.UserProfileInfoDto;
import ru.alexandr.BookingCinemaTickets.application.exception.RoleNotFoundException;
import ru.alexandr.BookingCinemaTickets.application.exception.UserNotFoundException;
import ru.alexandr.BookingCinemaTickets.application.mapper.UserProfileInfoMapper;
import ru.alexandr.BookingCinemaTickets.domain.model.Role;
import ru.alexandr.BookingCinemaTickets.domain.model.RoleUser;
import ru.alexandr.BookingCinemaTickets.domain.model.User;
import ru.alexandr.BookingCinemaTickets.infrastructure.repository.jpa.RoleRepository;
import ru.alexandr.BookingCinemaTickets.infrastructure.repository.jpa.RoleUserRepository;
import ru.alexandr.BookingCinemaTickets.infrastructure.repository.jpa.UserInfoRepository;
import ru.alexandr.BookingCinemaTickets.infrastructure.repository.jpa.UserRepository;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserProfileInfoMapper userProfileInfoMapper;
    private final RoleRepository roleRepository;
    private final RoleUserRepository roleUserRepository;

    public UserService(UserInfoRepository userInfoRepository,
                       UserRepository userRepository,
                       UserProfileInfoMapper userProfileInfoMapper,
                       RoleRepository roleRepository, RoleUserRepository roleUserRepository) {
        this.userRepository = userRepository;
        this.userProfileInfoMapper = userProfileInfoMapper;
        this.roleRepository = roleRepository;
        this.roleUserRepository = roleUserRepository;
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

    @Transactional
    public void assignRoleToUser(Long userId, Long roleId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(
                        String.format("Пользователь с id %s не найден", userId)
                ));
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RoleNotFoundException(
                        String.format("Роли с id %s не найдено", roleId)
                ));

        RoleUser assignRoleUser = new RoleUser(user, role);
        roleUserRepository.save(assignRoleUser);
    }
}
