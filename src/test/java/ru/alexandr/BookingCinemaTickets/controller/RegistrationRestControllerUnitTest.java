package ru.alexandr.BookingCinemaTickets.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import ru.alexandr.BookingCinemaTickets.application.dto.ApiErrorDto;
import ru.alexandr.BookingCinemaTickets.application.dto.RegisterDto;
import ru.alexandr.BookingCinemaTickets.application.dto.RoleDto;
import ru.alexandr.BookingCinemaTickets.application.dto.UserProfileInfoDto;
import ru.alexandr.BookingCinemaTickets.application.exception.RoleNotFoundException;
import ru.alexandr.BookingCinemaTickets.application.exception.UsernameAlreadyTakenException;
import ru.alexandr.BookingCinemaTickets.application.service.RegistrationService;
import ru.alexandr.BookingCinemaTickets.controller.rest.RegistrationRestController;
import ru.alexandr.BookingCinemaTickets.infrastructure.security.jwt.JwtAuthenticationFilter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = RegistrationRestController.class)
@AutoConfigureMockMvc(addFilters = false)
class RegistrationRestControllerUnitTest {
    private final String createUserWithInfoUrl = "/api/registration";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockitoBean
    private RegistrationService registrationService;
    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Test
    void createUserWithInfo_ShouldReturnUserProfile() throws Exception {
        RegisterDto registerDto = new RegisterDto(
                "username",
                "securityPassword",
                "email@gmail.com",
                "+79111222333"
        );

        UserProfileInfoDto userProfileInfoDto = new UserProfileInfoDto(
                1L,
                registerDto.username(),
                List.of(new RoleDto(1L, "ROLE_USER")),
                registerDto.email(),
                registerDto.phoneNumber(),
                LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME)
        );

        when(registrationService.register(registerDto)).thenReturn(userProfileInfoDto);

        MvcResult result = mockMvc.perform(post(createUserWithInfoUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDto)))
                .andExpect(status().is(200))
                .andReturn();

        String content = result.getResponse().getContentAsString();
        UserProfileInfoDto actualUserProfileDto = objectMapper.readValue(content, UserProfileInfoDto.class);

        assertThat(actualUserProfileDto).isEqualTo(userProfileInfoDto);
    }

    @Test
    void createUserWithInfo_ShouldReturnUserProfile_WhenOptionalParametersNotSpecified() throws Exception {
        RegisterDto registerDto = new RegisterDto(
                "username",
                "securityPassword",
                null,
                null
        );

        mockMvc.perform(post(createUserWithInfoUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDto)))
                .andExpect(status().is(200));
    }

    @Test
    void createUserWithInfo_ShouldReturn400Code_WhenRequiredParametersNotSpecified() throws Exception {
        RegisterDto registerDto = new RegisterDto(
                null,
                null,
                null,
                null
        );

        MvcResult result = mockMvc.perform(post(createUserWithInfoUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDto)))
                .andExpect(status().is(400))
                .andReturn();

        String content = result.getResponse().getContentAsString();
        ApiErrorDto error = objectMapper.readValue(content, ApiErrorDto.class);

        assertThat(error.exceptionName()).isEqualTo(MethodArgumentNotValidException.class.getName());
        assertThat(error.stackTrace()).isNotEmpty();
        assertThat(error.statusCode()).isEqualTo(400);
        assertThat(error.path()).isEqualTo(createUserWithInfoUrl);
    }

    @Test
    void createUserWithInfo_ShouldReturn404Code_WhenSpecifiedRoleIdNotFound() throws Exception {
        RegisterDto registerDto = new RegisterDto(
                "username",
                "securityPassword",
                null,
                null
        );

        when(registrationService.register(registerDto))
                .thenThrow(new RoleNotFoundException(""));

        MvcResult result = mockMvc.perform(post(createUserWithInfoUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDto)))
                .andExpect(status().is(404))
                .andReturn();

        String content = result.getResponse().getContentAsString();
        ApiErrorDto error = objectMapper.readValue(content, ApiErrorDto.class);

        assertThat(error.exceptionName()).isEqualTo(RoleNotFoundException.class.getName());
        assertThat(error.stackTrace()).isNotEmpty();
        assertThat(error.statusCode()).isEqualTo(404);
        assertThat(error.path()).isEqualTo(createUserWithInfoUrl);
    }

    @Test
    void createUserWithInfo_ShouldReturn409Code_WhenSpecifiedUsernameAlreadyTaken() throws Exception {
        RegisterDto registerDto = new RegisterDto(
                "username",
                "securityPassword",
                null,
                null
        );

        when(registrationService.register(registerDto))
                .thenThrow(new UsernameAlreadyTakenException(""));

        MvcResult result = mockMvc.perform(post(createUserWithInfoUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDto)))
                .andExpect(status().is(409))
                .andReturn();

        String content = result.getResponse().getContentAsString();
        ApiErrorDto error = objectMapper.readValue(content, ApiErrorDto.class);

        assertThat(error.exceptionName()).isEqualTo(UsernameAlreadyTakenException.class.getName());
        assertThat(error.stackTrace()).isNotEmpty();
        assertThat(error.statusCode()).isEqualTo(409);
        assertThat(error.path()).isEqualTo(createUserWithInfoUrl);
    }

    @Test
    void createUserWithInfo_ShouldReturn500Code_WhenThrowUnplannedException() throws Exception {
        RegisterDto registerDto = new RegisterDto(
                "username",
                "securityPassword",
                null,
                null
        );

        when(registrationService.register(registerDto))
                .thenThrow(new IllegalArgumentException(""));

        MvcResult result = mockMvc.perform(post(createUserWithInfoUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDto)))
                .andExpect(status().is(500))
                .andReturn();

        String content = result.getResponse().getContentAsString();
        ApiErrorDto error = objectMapper.readValue(content, ApiErrorDto.class);

        assertThat(error.exceptionName()).isEqualTo(IllegalArgumentException.class.getName());
        assertThat(error.stackTrace()).isNotEmpty();
        assertThat(error.statusCode()).isEqualTo(500);
        assertThat(error.path()).isEqualTo(createUserWithInfoUrl);
    }
}