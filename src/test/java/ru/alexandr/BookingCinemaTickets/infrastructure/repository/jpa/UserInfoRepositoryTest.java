package ru.alexandr.BookingCinemaTickets.infrastructure.repository.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ru.alexandr.BookingCinemaTickets.domain.model.User;
import ru.alexandr.BookingCinemaTickets.domain.model.UserInfo;
import ru.alexandr.BookingCinemaTickets.testConfiguration.PostgresTestContainerConfiguration;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(PostgresTestContainerConfiguration.class)
class UserInfoRepositoryTest {

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private UserRepository userRepository;

    @PersistenceContext
    private EntityManager entityManager;

    private UserInfo userInfoWithEmail;
    private UserInfo userInfoWithoutEmail;

    @BeforeEach
    void setUp() {
        assertThat(userInfoRepository.count()).isZero();

        User userWithEmail = userRepository.save(new User("user1", "hash"));
        userInfoWithEmail = new UserInfo(
                userWithEmail,
                LocalDateTime.of(2024, 1, 1, 1, 1, 1)
        );
        userInfoWithEmail.setEmail("test@example.com");
        userInfoWithEmail = userInfoRepository.save(userInfoWithEmail);

        User userWithoutEmail = userRepository.save(new User("user2", "hash"));
        userInfoWithoutEmail = new UserInfo(
                userWithoutEmail,
                LocalDateTime.of(2024, 1, 1, 1, 1, 1)
        );
        userInfoWithoutEmail = userInfoRepository.save(userInfoWithoutEmail);
    }

    @Test
    void findByEmail_ShouldReturnUserInfo_WhenGiveExistEmail() {
        Optional<UserInfo> foundUserInfo = userInfoRepository.findByEmail("test@example.com");

        assertThat(foundUserInfo).isPresent();
        assertThat(foundUserInfo.get().getEmail()).isEqualTo("test@example.com");
        assertThat(foundUserInfo.get()).isEqualTo(userInfoWithEmail);
    }

    @Test
    void findByEmail_ShouldReturnEmptyOptional_WhenNoUserWithEmail() {
        Optional<UserInfo> foundUserInfo = userInfoRepository.findByEmail("notexist@example.com");

        assertThat(foundUserInfo).isEmpty();
    }

    @Test
    void findByEmail_ShouldReturnUserInfo_WhenFindUserWithoutEmail() {
        Optional<UserInfo> foundUserInfo = userInfoRepository.findByEmail(null);

        assertThat(foundUserInfo).isPresent();
        assertThat(foundUserInfo.get().getEmail()).isEqualTo(null);
        assertThat(foundUserInfo.get()).isEqualTo(userInfoWithoutEmail);
    }
}