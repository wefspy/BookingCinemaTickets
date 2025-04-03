package ru.alexandr.BookingCinemaTickets.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.alexandr.BookingCinemaTickets.domain.User;
import ru.alexandr.BookingCinemaTickets.domain.UserInfo;
import ru.alexandr.BookingCinemaTickets.repository.UserRepository;

import java.time.LocalDateTime;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(@Autowired UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public void createUserWithInfo(String username, String password, String email, String phoneNumber) {
        User user = new User(
                username,
                password
        );

        UserInfo userInfo = new UserInfo(
                user,
                LocalDateTime.now()
        );
        userInfo.setEmail(email);
        userInfo.setPhoneNumber(phoneNumber);

        userRepository.save(user);
    }
}
