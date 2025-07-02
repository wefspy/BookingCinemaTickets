package ru.alexandr.BookingCinemaTickets.controller.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.alexandr.BookingCinemaTickets.application.dto.session.SessionCreateDto;
import ru.alexandr.BookingCinemaTickets.application.dto.session.SessionDto;
import ru.alexandr.BookingCinemaTickets.application.dto.session.SessionPreviewDto;
import ru.alexandr.BookingCinemaTickets.application.dto.session.SessionUpdateDto;
import ru.alexandr.BookingCinemaTickets.application.exception.SessionNotFoundException;
import ru.alexandr.BookingCinemaTickets.application.service.SessionService;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = SessionRestController.class)
@AutoConfigureMockMvc(addFilters = false)
public class SessionRestControllerUnitTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockitoBean
    private SessionService sessionService;

    @Test
    void create_ShouldReturn201_WhenSuccess() throws Exception {
        SessionCreateDto createDto = mock(SessionCreateDto.class);
        SessionDto sessionDto = mock(SessionDto.class);
        when(sessionService.create(any(SessionCreateDto.class))).thenReturn(sessionDto);
        mockMvc.perform(post("/api/sessions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isCreated());
        verify(sessionService).create(any(SessionCreateDto.class));
    }

    @Test
    void getAllByHallId_ShouldReturn200() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        when(sessionService.getAllByHallId(eq(1L), any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.emptyList(), pageable, 0));
        mockMvc.perform(get("/api/halls/1/sessions"))
                .andExpect(status().isOk());
        verify(sessionService).getAllByHallId(eq(1L), any(Pageable.class));
    }

    @Test
    void getAllByMovieId_ShouldReturn200() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        when(sessionService.getAllByMovieId(eq(1L), any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.emptyList(), pageable, 0));
        mockMvc.perform(get("/api/movies/1/sessions"))
                .andExpect(status().isOk());
        verify(sessionService).getAllByMovieId(eq(1L), any(Pageable.class));
    }

    @Test
    void getById_ShouldReturn200_WhenExists() throws Exception {
        SessionDto sessionDto = mock(SessionDto.class);
        when(sessionService.getById(1L)).thenReturn(sessionDto);
        mockMvc.perform(get("/api/sessions/1"))
                .andExpect(status().isOk());
        verify(sessionService).getById(1L);
    }

    @Test
    void getById_ShouldReturn404_WhenNotFound() throws Exception {
        when(sessionService.getById(2L)).thenThrow(new SessionNotFoundException("Not found"));
        mockMvc.perform(get("/api/sessions/2"))
                .andExpect(status().is(500));
        verify(sessionService).getById(2L);
    }

    @Test
    void update_ShouldReturn200_WhenSuccess() throws Exception {
        SessionPreviewDto previewDto = mock(SessionPreviewDto.class);
        when(sessionService.update(eq(1L), any(SessionUpdateDto.class))).thenReturn(previewDto);
        SessionUpdateDto updateDto = mock(SessionUpdateDto.class);
        mockMvc.perform(put("/api/sessions/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk());
        verify(sessionService).update(eq(1L), any(SessionUpdateDto.class));
    }

    @Test
    void update_ShouldReturn404_WhenNotFound() throws Exception {
        when(sessionService.update(eq(2L), any(SessionUpdateDto.class)))
                .thenThrow(new SessionNotFoundException("Not found"));
        SessionUpdateDto updateDto = mock(SessionUpdateDto.class);
        mockMvc.perform(put("/api/sessions/2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().is(500));
        verify(sessionService).update(eq(2L), any(SessionUpdateDto.class));
    }

    @Test
    void delete_ShouldReturn200_WhenSuccess() throws Exception {
        doNothing().when(sessionService).delete(1L);
        mockMvc.perform(delete("/api/sessions/1"))
                .andExpect(status().isOk());
        verify(sessionService).delete(1L);
    }

    @Test
    void delete_ShouldReturn404_WhenNotFound() throws Exception {
        doThrow(new SessionNotFoundException("Not found")).when(sessionService).delete(2L);
        mockMvc.perform(delete("/api/sessions/2"))
                .andExpect(status().is(500));
        verify(sessionService).delete(2L);
    }
} 