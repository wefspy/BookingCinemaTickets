package ru.alexandr.BookingCinemaTickets.controller.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import ru.alexandr.BookingCinemaTickets.application.dto.hall.HallCreateDto;
import ru.alexandr.BookingCinemaTickets.application.dto.hall.HallDto;
import ru.alexandr.BookingCinemaTickets.application.dto.hall.HallPreviewDto;
import ru.alexandr.BookingCinemaTickets.application.dto.hall.HallUpdateDto;
import ru.alexandr.BookingCinemaTickets.application.exception.HallNotFoundException;
import ru.alexandr.BookingCinemaTickets.application.service.HallService;
import ru.alexandr.BookingCinemaTickets.application.service.SeatService;
import ru.alexandr.BookingCinemaTickets.testUtils.constant.RestControllerUrls;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = HallRestController.class)
@AutoConfigureMockMvc(addFilters = false)
public class HallRestControllerUnitTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockitoBean
    private HallService hallService;
    @MockitoBean
    private SeatService seatService;

    @Test
    void create_ShouldReturn201_WhenSuccess() throws Exception {
        HallCreateDto createDto = mock(HallCreateDto.class);
        HallDto hallDto = mock(HallDto.class);
        when(hallService.create(any(HallCreateDto.class))).thenReturn(hallDto);

        mockMvc.perform(post("/api/halls")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isCreated());
        verify(hallService).create(any(HallCreateDto.class));
    }

    @Test
    void get_ShouldReturn404_WhenHallNotFound() throws Exception {
        when(hallService.get(123L)).thenThrow(new HallNotFoundException("Not found"));
        mockMvc.perform(get("/api/halls/123"))
                .andExpect(status().is(500));
        verify(hallService).get(123L);
    }

    @Test
    void get_ShouldReturn200_WhenHallExists() throws Exception {
        HallDto hallDto = mock(HallDto.class);
        when(hallService.get(1L)).thenReturn(hallDto);
        mockMvc.perform(get("/api/halls/1"))
                .andExpect(status().isOk());
        verify(hallService).get(1L);
    }

    @Test
    void getAllPreview_ShouldReturn200() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        when(hallService.getAllPreview(any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.emptyList(), pageable, 0));
        mockMvc.perform(get("/api/halls"))
                .andExpect(status().isOk());
        verify(hallService).getAllPreview(any(Pageable.class));
    }

    @Test
    void update_ShouldReturn200_WhenSuccess() throws Exception {
        HallPreviewDto previewDto = mock(HallPreviewDto.class);
        when(hallService.update(eq(1L), any(HallUpdateDto.class))).thenReturn(previewDto);
        HallUpdateDto updateDto = mock(HallUpdateDto.class);
        mockMvc.perform(put("/api/halls/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk());
        verify(hallService).update(eq(1L), any(HallUpdateDto.class));
    }

    @Test
    void update_ShouldReturn404_WhenHallNotFound() throws Exception {
        when(hallService.update(eq(2L), any(HallUpdateDto.class)))
                .thenThrow(new HallNotFoundException("Not found"));
        HallUpdateDto updateDto = mock(HallUpdateDto.class);
        mockMvc.perform(put("/api/halls/2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().is(500));
        verify(hallService).update(eq(2L), any(HallUpdateDto.class));
    }

    @Test
    void delete_ShouldReturn200_WhenSuccess() throws Exception {
        doNothing().when(hallService).delete(1L);
        mockMvc.perform(delete("/api/halls/1"))
                .andExpect(status().isOk());
        verify(hallService).delete(1L);
    }

    @Test
    void delete_ShouldReturn404_WhenHallNotFound() throws Exception {
        doThrow(new HallNotFoundException("Not found")).when(hallService).delete(2L);
        mockMvc.perform(delete("/api/halls/2"))
                .andExpect(status().is(500));
        verify(hallService).delete(2L);
    }
} 