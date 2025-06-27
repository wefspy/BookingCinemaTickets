package ru.alexandr.BookingCinemaTickets.controller.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import ru.alexandr.BookingCinemaTickets.application.dto.LoginRequestDto;
import ru.alexandr.BookingCinemaTickets.application.dto.LoginResponseDto;
import ru.alexandr.BookingCinemaTickets.application.dto.RegisterDto;
import ru.alexandr.BookingCinemaTickets.application.service.AuthService;
import ru.alexandr.BookingCinemaTickets.application.service.RegistrationService;
import ru.alexandr.BookingCinemaTickets.testUtils.annotation.PostgreSQLTestContainer;
import ru.alexandr.BookingCinemaTickets.testUtils.annotation.TestActiveProfile;
import ru.alexandr.BookingCinemaTickets.testUtils.asserts.LoginResponseDtoAssert;
import ru.alexandr.BookingCinemaTickets.testUtils.constant.RestControllerUrls;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestActiveProfile
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@PostgreSQLTestContainer
public class AuthRestControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RegistrationService registrationService;
    @Autowired
    private AuthService authService;

    private final RegisterDto registerDto = new RegisterDto(
            "username",
            "password",
            "email@example.com",
            null
    );
    private final LoginRequestDto loginRequestDto = new LoginRequestDto(registerDto.username(), registerDto.password());

    @BeforeEach
    void setUp() {
        registrationService.register(registerDto);
    }

    @Test
    void loginAdnRefresh_ShouldLoginAndGivenRefreshTokenMakeRefresh() throws Exception {
        MvcResult resultLogin = mockMvc.perform(post(RestControllerUrls.AUTH_LOGIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequestDto)))
                .andExpect(status().is(200))
                .andReturn();

        String responseBodyLogin = resultLogin.getResponse().getContentAsString();
        LoginResponseDto actualLoginDto = objectMapper.readValue(responseBodyLogin, LoginResponseDto.class);

        MvcResult resultRefresh = mockMvc.perform(post(RestControllerUrls.AUTH_REFRESH)
                        .param("refreshToken", actualLoginDto.refreshToken()))
                .andExpect(status().is(200))
                .andReturn();

        String responseBodyRefresh = resultRefresh.getResponse().getContentAsString();
        LoginResponseDto actualRefreshDto = objectMapper.readValue(responseBodyRefresh, LoginResponseDto.class);

        LoginResponseDtoAssert.assertThat(actualLoginDto).isNotEqualTo(actualRefreshDto);
    }

    @Test
    void refreshAndRefresh_ShouldMakeRefreshAndGivenRefreshTokenMakeRefresh() throws Exception {
        LoginResponseDto actualLoginDto = authService.login(loginRequestDto);

        MvcResult resultFirstRefresh = mockMvc.perform(post(RestControllerUrls.AUTH_REFRESH)
                        .param("refreshToken", actualLoginDto.refreshToken()))
                .andExpect(status().is(200))
                .andReturn();

        String responseBodyFirstRefresh = resultFirstRefresh.getResponse().getContentAsString();
        LoginResponseDto actualFirstRefreshDto = objectMapper.readValue(responseBodyFirstRefresh, LoginResponseDto.class);

        MvcResult resultSecondRefresh = mockMvc.perform(post(RestControllerUrls.AUTH_REFRESH)
                        .param("refreshToken", actualFirstRefreshDto.refreshToken()))
                .andExpect(status().is(200))
                .andReturn();

        String responseBodySecondRefresh = resultSecondRefresh.getResponse().getContentAsString();
        LoginResponseDto actualSecondRefreshDto = objectMapper.readValue(responseBodySecondRefresh, LoginResponseDto.class);

        LoginResponseDtoAssert.assertThat(actualFirstRefreshDto).isNotEqualTo(actualSecondRefreshDto);
    }
}
