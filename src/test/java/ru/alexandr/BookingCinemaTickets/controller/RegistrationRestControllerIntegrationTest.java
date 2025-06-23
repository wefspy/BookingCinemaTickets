package ru.alexandr.BookingCinemaTickets.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import ru.alexandr.BookingCinemaTickets.application.dto.RegisterDto;
import ru.alexandr.BookingCinemaTickets.application.dto.UserProfileInfoDto;
import ru.alexandr.BookingCinemaTickets.testUtils.annotation.PostgreSQLTestContainer;
import ru.alexandr.BookingCinemaTickets.testUtils.annotation.TestActiveProfile;
import ru.alexandr.BookingCinemaTickets.testUtils.asserts.UserProfileInfoDtoAssert;
import ru.alexandr.BookingCinemaTickets.testUtils.url.ControllerUrls;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestActiveProfile
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@PostgreSQLTestContainer
public class RegistrationRestControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    private final RegisterDto registerDto = new RegisterDto(
            "username123",
            "securityPassword",
            "email@gmail.com",
            "+79111222333"
    );

    @Test
    void createUserWithInfo_ShouldCreateUser() throws Exception {
        LocalDateTime startRegister = LocalDateTime.now();
        MvcResult result = mockMvc.perform(post(ControllerUrls.REGISTRATION)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDto)))
                .andExpect(status().is(200))
                .andReturn();
        LocalDateTime endRegister = LocalDateTime.now();


        String response = result.getResponse().getContentAsString();
        UserProfileInfoDto actualDto = objectMapper.readValue(response, UserProfileInfoDto.class);

        UserProfileInfoDtoAssert.assertThat(actualDto)
                .isNotNull()
                .hasRegisterDto(registerDto, startRegister, endRegister)
                .hasBaseRoles();
    }
}
