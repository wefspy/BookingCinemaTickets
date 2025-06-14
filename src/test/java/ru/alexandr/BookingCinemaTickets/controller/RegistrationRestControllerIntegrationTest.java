package ru.alexandr.BookingCinemaTickets.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.alexandr.BookingCinemaTickets.application.dto.RegisterDto;
import ru.alexandr.BookingCinemaTickets.application.dto.UserProfileInfoDto;
import ru.alexandr.BookingCinemaTickets.infrastructure.repository.jpa.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class RegistrationRestControllerIntegrationTest {

    private final String createUserWithInfoUrl = "/api/registration";
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    void createUserWithInfo_ShouldCreateUser() throws Exception {
        RegisterDto registerDto = new RegisterDto(
                "username123",
                "securityPassword",
                "email@gmail.com",
                "+79111222333"
        );

        MvcResult result = mockMvc.perform(post(createUserWithInfoUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDto)))
                .andExpect(status().is(200))
                .andReturn();

        String response = result.getResponse().getContentAsString();
        UserProfileInfoDto userProfileInfoDto = objectMapper.readValue(response, UserProfileInfoDto.class);

        assertThat(userProfileInfoDto.userName()).isEqualTo(registerDto.username());
        assertThat(userProfileInfoDto.roles())
                .hasSize(1);
        assertThat(userProfileInfoDto.email()).isEqualTo(registerDto.email());
        assertThat(userProfileInfoDto.phoneNumber()).isEqualTo(registerDto.phoneNumber());
    }
}
