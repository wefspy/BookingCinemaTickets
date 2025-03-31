package ru.alexandr.BookingCinemaTickets.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.alexandr.BookingCinemaTickets.domain.User;
import ru.alexandr.BookingCinemaTickets.domain.UserInfo;
import ru.alexandr.BookingCinemaTickets.repository.UserInfoRepository;
import ru.alexandr.BookingCinemaTickets.repository.UserRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class UserServiceTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private UserService userService;

    @BeforeEach
    void setUp() {
        assertThat(userRepository.count()).isZero();
        assertThat(userInfoRepository.count()).isZero();
    }

    @AfterEach
    void tearDown() {
        userInfoRepository.deleteAll();
        userRepository.deleteAll();

        assertThat(userRepository.count()).isZero();
        assertThat(userInfoRepository.count()).isZero();
    }

    @Test
    void createUserWithInfo_ShouldSaveUserAndUserInfo() {
        userService.createUserWithInfo(
                "testUser",
                "hashedPassword",
                "test@example.com",
                "123456789"
        );

        Optional<User> savedUser = userRepository.findByUsername("testUser");
        Optional<UserInfo> savedUserInfo = userInfoRepository.findByEmail("test@example.com");

        assertThat(savedUser).isPresent();
        assertThat(savedUserInfo).isPresent();
        assertThat(savedUser.get().getUserInfo()).isEqualTo(savedUserInfo.get());
    }

    @Test
    void createUserWithInfo_ShouldRollbackTransaction_OnConstraintViolation() {
        userService.createUserWithInfo(
                "duplicateUsername",
                "password1",
                "test1@example.com",
                "111111"
        );

        userService.createUserWithInfo(
                "duplicateUsername",
                "password2",
                "test2@example.com",
                "222222"
        );

        assertThat(userRepository.count()).isEqualTo(1);
        assertThat(userInfoRepository.count()).isEqualTo(1);
    }
}