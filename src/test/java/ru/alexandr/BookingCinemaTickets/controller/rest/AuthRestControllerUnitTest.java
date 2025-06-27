package ru.alexandr.BookingCinemaTickets.controller.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.MalformedJwtException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import ru.alexandr.BookingCinemaTickets.application.dto.ApiErrorDto;
import ru.alexandr.BookingCinemaTickets.application.dto.LoginRequestDto;
import ru.alexandr.BookingCinemaTickets.application.dto.LoginResponseDto;
import ru.alexandr.BookingCinemaTickets.application.exception.UserNotFoundException;
import ru.alexandr.BookingCinemaTickets.application.service.AuthService;
import ru.alexandr.BookingCinemaTickets.infrastructure.security.filter.JwtAuthenticationFilter;
import ru.alexandr.BookingCinemaTickets.testUtils.asserts.ApiErrorDtoAssert;
import ru.alexandr.BookingCinemaTickets.testUtils.constant.RestControllerUrls;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AuthRestController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AuthRestControllerUnitTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockitoBean
    private AuthService authService;
    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Test
    void login_ShouldReturnLoginResponseDto_WhenLoginSuccess() throws Exception {
        LoginRequestDto loginDto = new LoginRequestDto("username", "password");
        LoginResponseDto responseDto = new LoginResponseDto("accessToken", "refreshToken");

        when(authService.login(loginDto)).thenReturn(responseDto);

        MvcResult result = mockMvc.perform(post(RestControllerUrls.AUTH_LOGIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().is(200))
                .andReturn();

        String content = result.getResponse().getContentAsString();
        LoginResponseDto actualResponseDto = objectMapper.readValue(content, LoginResponseDto.class);

        Assertions.assertThat(actualResponseDto).isEqualTo(responseDto);
        verify(authService).login(loginDto);
    }

    @Test
    void login_ShouldReturn400Code_WhenUsernameNotSpecified() throws Exception {
        LoginRequestDto loginDto = new LoginRequestDto(null, "password");

        int statusCode = 400;

        MvcResult result = mockMvc.perform(post(RestControllerUrls.AUTH_LOGIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().is(statusCode))
                .andReturn();

        String content = result.getResponse().getContentAsString();
        ApiErrorDto actualError = objectMapper.readValue(content, ApiErrorDto.class);

        assertError(actualError, MethodArgumentNotValidException.class.getName(), statusCode, RestControllerUrls.AUTH_LOGIN);
        verify(authService, never()).login(loginDto);
    }

    @Test
    void login_ShouldReturn400Code_WhenAuthenticationFailed() throws Exception {
        LoginRequestDto loginDto = new LoginRequestDto("username", null);

        int statusCode = 400;

        MvcResult result = mockMvc.perform(post(RestControllerUrls.AUTH_LOGIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().is(statusCode))
                .andReturn();

        String content = result.getResponse().getContentAsString();
        ApiErrorDto actualError = objectMapper.readValue(content, ApiErrorDto.class);

        assertError(actualError, MethodArgumentNotValidException.class.getName(), statusCode, RestControllerUrls.AUTH_LOGIN);
        verify(authService, never()).login(loginDto);
    }

    @Test
    void login_ShouldReturn401Code_WhenPasswordNotSpecified() throws Exception {
        LoginRequestDto loginDto = new LoginRequestDto("username", "password");

        int statusCode = 401;
        BadCredentialsException exception = new BadCredentialsException("bad credentials");

        when(authService.login(loginDto)).thenThrow(exception);

        MvcResult result = mockMvc.perform(post(RestControllerUrls.AUTH_LOGIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().is(statusCode))
                .andReturn();

        String content = result.getResponse().getContentAsString();
        ApiErrorDto actualError = objectMapper.readValue(content, ApiErrorDto.class);

        assertError(actualError, exception.getClass().getName(), statusCode, RestControllerUrls.AUTH_LOGIN);
    }

    @Test
    void refresh_ShouldReturnLoginResponseDto_WhenRefreshSuccess() throws Exception {
        String refreshToken = "refreshToken";
        LoginResponseDto responseDto = new LoginResponseDto("accessToken", "newRefreshToken");

        when(authService.refresh(refreshToken)).thenReturn(responseDto);

        MvcResult result = mockMvc.perform(post(RestControllerUrls.AUTH_REFRESH)
                        .param("refreshToken", refreshToken))
                .andExpect(status().is(200))
                .andReturn();

        String content = result.getResponse().getContentAsString();
        LoginResponseDto actualDto = objectMapper.readValue(content, LoginResponseDto.class);

        Assertions.assertThat(actualDto).isEqualTo(responseDto);
        verify(authService).refresh(refreshToken);
    }

    @Test
    void refresh_ShouldReturn400Code_WhenNotGivenRefreshToken() throws Exception {
        String refreshToken = null;
        int statusCode = 400;

        MvcResult result = mockMvc.perform(post(RestControllerUrls.AUTH_REFRESH)
                        .param("refreshToken", refreshToken))
                .andExpect(status().is(statusCode))
                .andReturn();

        String content = result.getResponse().getContentAsString();
        ApiErrorDto actualError = objectMapper.readValue(content, ApiErrorDto.class);

        assertError(actualError, MissingServletRequestParameterException.class.getName(), statusCode, RestControllerUrls.AUTH_REFRESH);
    }

    @Test
    void refresh_ShouldReturn401Code_WhenGivenInvalidToken() throws Exception {
        String refreshToken = "refreshToken";
        int statusCode = 401;
        MalformedJwtException exception = new MalformedJwtException("invalidToken");

        when(authService.refresh(refreshToken)).thenThrow(exception);

        MvcResult result = mockMvc.perform(post(RestControllerUrls.AUTH_REFRESH)
                        .param("refreshToken", refreshToken))
                .andExpect(status().is(statusCode))
                .andReturn();

        String content = result.getResponse().getContentAsString();
        ApiErrorDto actualError = objectMapper.readValue(content, ApiErrorDto.class);

        assertError(actualError, exception.getClass().getName(), statusCode, RestControllerUrls.AUTH_REFRESH);
    }

    @Test
    void refresh_ShouldReturn404Code_WhenUserNotFound() throws Exception {
        String refreshToken = "refreshToken";
        int statusCode = 404;
        UserNotFoundException exception = new UserNotFoundException("User not found");

        when(authService.refresh(refreshToken)).thenThrow(exception);

        MvcResult result = mockMvc.perform(post(RestControllerUrls.AUTH_REFRESH)
                        .param("refreshToken", refreshToken))
                .andExpect(status().is(statusCode))
                .andReturn();

        String content = result.getResponse().getContentAsString();
        ApiErrorDto actualError = objectMapper.readValue(content, ApiErrorDto.class);

        assertError(actualError, exception.getClass().getName(), statusCode, RestControllerUrls.AUTH_REFRESH);
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
