package ru.alexandr.BookingCinemaTickets.controller.rest;

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
import ru.alexandr.BookingCinemaTickets.infrastructure.security.filter.JwtAuthenticationFilter;
import ru.alexandr.BookingCinemaTickets.testUtils.asserts.ApiErrorDtoAssert;
import ru.alexandr.BookingCinemaTickets.testUtils.constant.RestControllerUrls;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = RegistrationRestController.class)
@AutoConfigureMockMvc(addFilters = false)
class RegistrationRestControllerUnitTest {
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
                List.of(new RoleDto(1L, "USER")),
                registerDto.email(),
                registerDto.phoneNumber(),
                LocalDateTime.now()
        );

        when(registrationService.register(registerDto)).thenReturn(userProfileInfoDto);

        MvcResult result = mockMvc.perform(post(RestControllerUrls.REGISTRATION)
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

        mockMvc.perform(post(RestControllerUrls.REGISTRATION)
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

        int expectedStatusCode = 400;
        MvcResult result = mockMvc.perform(post(RestControllerUrls.REGISTRATION)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDto)))
                .andExpect(status().is(expectedStatusCode))
                .andReturn();

        String content = result.getResponse().getContentAsString();
        ApiErrorDto error = objectMapper.readValue(content, ApiErrorDto.class);

        assertError(error, MethodArgumentNotValidException.class.getName(), expectedStatusCode, RestControllerUrls.REGISTRATION);
    }

    @Test
    void createUserWithInfo_ShouldReturn404Code_WhenSpecifiedRoleIdNotFound() throws Exception {
        RegisterDto registerDto = new RegisterDto(
                "username",
                "securityPassword",
                null,
                null
        );

        int expectedStatusCode = 404;
        Exception exception = new RoleNotFoundException("");
        when(registrationService.register(registerDto)).thenThrow(exception);

        MvcResult result = mockMvc.perform(post(RestControllerUrls.REGISTRATION)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDto)))
                .andExpect(status().is(expectedStatusCode))
                .andReturn();

        String content = result.getResponse().getContentAsString();
        ApiErrorDto error = objectMapper.readValue(content, ApiErrorDto.class);

        assertError(error, exception.getClass().getName(), expectedStatusCode, RestControllerUrls.REGISTRATION);
    }

    @Test
    void createUserWithInfo_ShouldReturn409Code_WhenSpecifiedUsernameAlreadyTaken() throws Exception {
        RegisterDto registerDto = new RegisterDto(
                "username",
                "securityPassword",
                null,
                null
        );

        int expectedStatusCode = 409;
        Exception exception = new UsernameAlreadyTakenException("");
        when(registrationService.register(registerDto)).thenThrow(exception);

        MvcResult result = mockMvc.perform(post(RestControllerUrls.REGISTRATION)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDto)))
                .andExpect(status().is(expectedStatusCode))
                .andReturn();

        String content = result.getResponse().getContentAsString();
        ApiErrorDto error = objectMapper.readValue(content, ApiErrorDto.class);

        assertError(error, exception.getClass().getName(), expectedStatusCode, RestControllerUrls.REGISTRATION);
    }

    @Test
    void createUserWithInfo_ShouldReturn500Code_WhenThrowUnplannedException() throws Exception {
        RegisterDto registerDto = new RegisterDto(
                "username",
                "securityPassword",
                null,
                null
        );

        int expectedStatusCode = 500;
        Exception exception = new IllegalArgumentException("");
        when(registrationService.register(registerDto)).thenThrow(exception);

        MvcResult result = mockMvc.perform(post(RestControllerUrls.REGISTRATION)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDto)))
                .andExpect(status().is(expectedStatusCode))
                .andReturn();

        String content = result.getResponse().getContentAsString();
        ApiErrorDto error = objectMapper.readValue(content, ApiErrorDto.class);

        assertError(error, exception.getClass().getName(), expectedStatusCode, RestControllerUrls.REGISTRATION);
    }

    void assertError(ApiErrorDto error,
                     String exceptionName,
                     int statusCode,
                     String path) {
        ApiErrorDtoAssert.assertThat(error)
                .isNotNull()
                .exceptionNameIsEqualTo(exceptionName)
                .stackTraceIsNotEmpty()
                .statusCodeIsEqualTo(statusCode)
                .pathIsEqualTo(path);
    }
}