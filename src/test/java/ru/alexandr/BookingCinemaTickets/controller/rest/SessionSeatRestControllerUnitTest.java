package ru.alexandr.BookingCinemaTickets.controller.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.alexandr.BookingCinemaTickets.application.dto.sessionSeat.SessionSeatCreateDto;
import ru.alexandr.BookingCinemaTickets.application.dto.sessionSeat.SessionSeatDto;
import ru.alexandr.BookingCinemaTickets.application.dto.sessionSeat.SessionSeatUpdateDto;
import ru.alexandr.BookingCinemaTickets.application.exception.SessionSeatNotFoundException;
import ru.alexandr.BookingCinemaTickets.application.service.SessionSeatService;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = SessionSeatRestController.class)
@AutoConfigureMockMvc(addFilters = false)
public class SessionSeatRestControllerUnitTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockitoBean
    private SessionSeatService sessionSeatService;

    @Test
    void create_ShouldReturn201_WhenSuccess() throws Exception {
        List<SessionSeatCreateDto> createDtos = Collections.emptyList();
        List<SessionSeatDto> seatDtos = Collections.emptyList();
        when(sessionSeatService.create(eq(1L), any(List.class))).thenReturn(seatDtos);
        mockMvc.perform(post("/api/sessions/1/seats")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDtos)))
                .andExpect(status().isCreated());
        verify(sessionSeatService).create(eq(1L), any(List.class));
    }

    @Test
    void update_ShouldReturn200_WhenSuccess() throws Exception {
        SessionSeatUpdateDto updateDto = mock(SessionSeatUpdateDto.class);
        SessionSeatDto seatDto = mock(SessionSeatDto.class);
        when(sessionSeatService.update(eq(1L), eq(1L), any(SessionSeatUpdateDto.class))).thenReturn(seatDto);
        mockMvc.perform(put("/api/sessions/1/seats/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk());
        verify(sessionSeatService).update(eq(1L), eq(1L), any(SessionSeatUpdateDto.class));
    }

    @Test
    void update_ShouldReturn404_WhenNotFound() throws Exception {
        when(sessionSeatService.update(eq(2L), eq(2L), any(SessionSeatUpdateDto.class)))
                .thenThrow(new SessionSeatNotFoundException("Not found"));
        SessionSeatUpdateDto updateDto = mock(SessionSeatUpdateDto.class);
        mockMvc.perform(put("/api/sessions/2/seats/2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().is(500));
        verify(sessionSeatService).update(eq(2L), eq(2L), any(SessionSeatUpdateDto.class));
    }

    @Test
    void delete_ShouldReturn200_WhenSuccess() throws Exception {
        doNothing().when(sessionSeatService).delete(1L, 1L);
        mockMvc.perform(delete("/api/sessions/1/seats/1"))
                .andExpect(status().isOk());
        verify(sessionSeatService).delete(1L, 1L);
    }

    @Test
    void delete_ShouldReturn404_WhenNotFound() throws Exception {
        doThrow(new SessionSeatNotFoundException("Not found")).when(sessionSeatService).delete(2L, 2L);
        mockMvc.perform(delete("/api/sessions/2/seats/2"))
                .andExpect(status().is(500));
        verify(sessionSeatService).delete(2L, 2L);
    }
} 