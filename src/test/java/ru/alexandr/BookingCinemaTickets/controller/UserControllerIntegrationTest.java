package ru.alexandr.BookingCinemaTickets.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.alexandr.BookingCinemaTickets.domain.Role;
import ru.alexandr.BookingCinemaTickets.dto.RoleDto;
import ru.alexandr.BookingCinemaTickets.dto.UserProfileInfoDto;
import ru.alexandr.BookingCinemaTickets.dto.UserRegisterDto;
import ru.alexandr.BookingCinemaTickets.repository.RoleRepository;
import ru.alexandr.BookingCinemaTickets.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerIntegrationTest {

    private final String createUserWithInfoUrl = "/api/register";
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;

    private Role role;

    @BeforeEach
    void setUp() {
        role = roleRepository.save(new Role("ROLE_USER"));
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
        roleRepository.deleteAll();
    }

    @Test
    void createUserWithInfo_ShouldCreateUser() throws Exception {
        UserRegisterDto userRegisterDto = new UserRegisterDto(
                "username",
                "securityPassword",
                Set.of(role.getId()),
                "email@gmail.com",
                "+79111222333",
                LocalDateTime.now()
        );

        MvcResult result = mockMvc.perform(post(createUserWithInfoUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRegisterDto)))
                .andExpect(status().is(200))
                .andReturn();

        String response = result.getResponse().getContentAsString();
        UserProfileInfoDto userProfileInfoDto = objectMapper.readValue(response, UserProfileInfoDto.class);

        assertThat(userProfileInfoDto.userName()).isEqualTo(userRegisterDto.username());
        assertThat(userProfileInfoDto.roles())
                .hasSize(1)
                .extracting(RoleDto::id)
                .contains(role.getId());
        assertThat(userProfileInfoDto.email()).isEqualTo(userRegisterDto.email());
        assertThat(userProfileInfoDto.phoneNumber()).isEqualTo(userRegisterDto.phoneNumber());
    }
}
