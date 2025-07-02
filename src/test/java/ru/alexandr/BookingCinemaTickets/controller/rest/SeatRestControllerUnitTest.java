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
import ru.alexandr.BookingCinemaTickets.application.dto.seat.SeatCreateDto;
import ru.alexandr.BookingCinemaTickets.application.dto.seat.SeatDto;
import ru.alexandr.BookingCinemaTickets.application.exception.SeatNotFoundException;
import ru.alexandr.BookingCinemaTickets.application.service.SeatService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = SeatRestController.class)
@AutoConfigureMockMvc(addFilters = false)
public class SeatRestControllerUnitTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockitoBean
    private SeatService seatService;

    @Test
    void update_ShouldReturn200_WhenSuccess() throws Exception {
        SeatDto seatDto = mock(SeatDto.class);
        when(seatService.update(eq(1L), eq(1L), any(SeatCreateDto.class))).thenReturn(seatDto);
        SeatCreateDto updateDto = mock(SeatCreateDto.class);
        mockMvc.perform(put("/api/halls/1/seats/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk());
        verify(seatService).update(eq(1L), eq(1L), any(SeatCreateDto.class));
    }

    @Test
    void update_ShouldReturn404_WhenSeatNotFound() throws Exception {
        when(seatService.update(eq(2L), eq(2L), any(SeatCreateDto.class)))
                .thenThrow(new SeatNotFoundException("Not found"));
        SeatCreateDto updateDto = mock(SeatCreateDto.class);
        mockMvc.perform(put("/api/halls/2/seats/2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().is(500));
        verify(seatService).update(eq(2L), eq(2L), any(SeatCreateDto.class));
    }

    @Test
    void delete_ShouldReturn200_WhenSuccess() throws Exception {
        doNothing().when(seatService).delete(1L, 1L);
        mockMvc.perform(delete("/api/halls/1/seats/1"))
                .andExpect(status().isOk());
        verify(seatService).delete(1L, 1L);
    }

    @Test
    void delete_ShouldReturn404_WhenSeatNotFound() throws Exception {
        doThrow(new SeatNotFoundException("Not found")).when(seatService).delete(2L, 2L);
        mockMvc.perform(delete("/api/halls/2/seats/2"))
                .andExpect(status().is(500));
        verify(seatService).delete(2L, 2L);
    }
} 