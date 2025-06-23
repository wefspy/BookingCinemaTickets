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
import ru.alexandr.BookingCinemaTickets.testUtils.annotation.PostgreSQLTestContainer;
import ru.alexandr.BookingCinemaTickets.testUtils.configuration.PostgresTestContainerConfiguration;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@PostgreSQLTestContainer
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
        User userWithEmail = userRepository.save(new User("user1", "hash"));
        userInfoWithEmail = new UserInfo(userWithEmail, LocalDateTime.now());
        userInfoWithEmail.setEmail("test@example.com");
        userInfoWithEmail = userInfoRepository.save(userInfoWithEmail);

        User userWithoutEmail = userRepository.save(new User("user2", "hash"));
        userInfoWithoutEmail = new UserInfo(userWithoutEmail, LocalDateTime.now());
        userInfoWithoutEmail = userInfoRepository.save(userInfoWithoutEmail);

        entityManager.flush();
        entityManager.clear();
    }

    @Test
    void findByEmail_ShouldReturnUserInfo_WhenGiveExistEmail() {
        Optional<UserInfo> foundUserInfo = userInfoRepository.findByEmail(userInfoWithEmail.getEmail());

        assertThat(foundUserInfo).isPresent();
        assertThat(foundUserInfo.get()).isEqualTo(userInfoWithEmail);
    }

    @Test
    void findByEmail_ShouldReturnEmptyOptional_WhenNoUserWithEmail() {
        Optional<UserInfo> foundUserInfo = userInfoRepository.findByEmail("notexist@example.com");

        assertThat(foundUserInfo).isEmpty();
    }
}