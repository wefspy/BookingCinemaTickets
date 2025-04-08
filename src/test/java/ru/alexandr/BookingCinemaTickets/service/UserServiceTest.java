package ru.alexandr.BookingCinemaTickets.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import ru.alexandr.BookingCinemaTickets.domain.User;
import ru.alexandr.BookingCinemaTickets.domain.UserInfo;
import ru.alexandr.BookingCinemaTickets.repository.UserInfoRepository;
import ru.alexandr.BookingCinemaTickets.repository.UserRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatException;

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
        String username = "testuser";
        String password = "hashedPassword";
        String email = "test@example.com";
        String phoneNumber = "123456789";

        userService.createUserWithInfo(
                username,
                password,
                email,
                phoneNumber
        );

        Optional<User> savedUser = userRepository.findByUsername(username);
        Optional<UserInfo> savedUserInfo = userInfoRepository.findByEmail("test@example.com");

        assertThat(savedUser).isPresent();
        assertThat(savedUserInfo).isPresent();
        assertThat(savedUser.get().getUserInfo()).isEqualTo(savedUserInfo.get());
    }

    @Test
    void createUserWithInfo_ShouldSaveCorrectUser() {
        String username = "testuser";
        String password = "securepass";
        String email = "test@example.com";
        String phoneNumber = "1234567890";

        userService.createUserWithInfo(
                username,
                password,
                email,
                phoneNumber);

        Optional<User> optionalUser = userRepository.findByUsername(username);
        assertThat(optionalUser).isPresent();

        User user = optionalUser.get();

        assertThat(user.getUsername()).isEqualTo(username);
        assertThat(user.equalsPassword(password)).isTrue();
    }

    @Test
    void createUserWithInfo_ShouldSaveCorrectUserInfo() {
        String username = "testuser";
        String password = "securepass";
        String email = "test@example.com";
        String phoneNumber = "1234567890";

        userService.createUserWithInfo(
                username,
                password,
                email,
                phoneNumber);

        Optional<User> optionalUser = userRepository.findByUsername(username);
        assertThat(optionalUser).isPresent();

        User user = optionalUser.get();
        UserInfo userInfo = user.getUserInfo();

        assertThat(userInfo).isNotNull();
        assertThat(userInfo.getEmail()).isEqualTo(email);
        assertThat(userInfo.getPhoneNumber()).isEqualTo(phoneNumber);
        assertThat(userInfo.getUser()).isEqualTo(user);
    }

    @Test
    void createUserWithInfo_ShouldRollbackTransaction_OnConstraintViolation() {
        String user1Username = "duplicateUsername";
        String user1Password = "password1";
        String user1Email = "test1@example.com";
        String user1PhoneNumber = "111111";

        userService.createUserWithInfo(
                user1Username,
                user1Password,
                user1Email,
                user1PhoneNumber
        );

        assertThatException().isThrownBy(() -> userService.createUserWithInfo(
                        user1Username,
                        "password2",
                        "test2@example.com",
                        "222222"))
                .isInstanceOf(DataIntegrityViolationException.class);

        assertThat(userRepository.count()).isEqualTo(1);
        assertThat(userInfoRepository.count()).isEqualTo(1);

        Optional<User> optionalUser = userRepository.findByUsername(user1Username);
        assertThat(optionalUser).isPresent();

        UserInfo userInfo = optionalUser.get().getUserInfo();
        assertThat(userInfo.getEmail()).isEqualTo(user1Email);
        assertThat(userInfo.getPhoneNumber()).isEqualTo(user1PhoneNumber);
    }
}