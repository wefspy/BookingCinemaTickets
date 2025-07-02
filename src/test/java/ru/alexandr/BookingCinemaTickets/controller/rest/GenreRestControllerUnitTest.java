package ru.alexandr.BookingCinemaTickets.controller.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.alexandr.BookingCinemaTickets.application.dto.genre.GenreCreateDto;
import ru.alexandr.BookingCinemaTickets.application.dto.genre.GenreDto;
import ru.alexandr.BookingCinemaTickets.application.exception.GenreNotFoundException;
import ru.alexandr.BookingCinemaTickets.application.service.GenreService;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = GenreRestController.class)
@AutoConfigureMockMvc(addFilters = false)
public class GenreRestControllerUnitTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockitoBean
    private GenreService genreService;

    @Test
    void create_ShouldReturn201_WhenSuccess() throws Exception {
        GenreCreateDto createDto = mock(GenreCreateDto.class);
        GenreDto genreDto = mock(GenreDto.class);
        when(genreService.create(any(GenreCreateDto.class))).thenReturn(genreDto);
        mockMvc.perform(post("/api/genres")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isCreated());
        verify(genreService).create(any(GenreCreateDto.class));
    }

    @Test
    void getAll_ShouldReturn200() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        when(genreService.getAll(any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.emptyList(), pageable, 0));
        mockMvc.perform(get("/api/genres"))
                .andExpect(status().isOk());
        verify(genreService).getAll(any(Pageable.class));
    }

    @Test
    void update_ShouldReturn200_WhenSuccess() throws Exception {
        GenreDto genreDto = mock(GenreDto.class);
        when(genreService.update(eq(1L), any(GenreCreateDto.class))).thenReturn(genreDto);
        GenreCreateDto updateDto = mock(GenreCreateDto.class);
        mockMvc.perform(put("/api/genres/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk());
        verify(genreService).update(eq(1L), any(GenreCreateDto.class));
    }

    @Test
    void update_ShouldReturn404_WhenGenreNotFound() throws Exception {
        when(genreService.update(eq(2L), any(GenreCreateDto.class)))
                .thenThrow(new GenreNotFoundException("Not found"));
        GenreCreateDto updateDto = mock(GenreCreateDto.class);
        mockMvc.perform(put("/api/genres/2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().is(500));
        verify(genreService).update(eq(2L), any(GenreCreateDto.class));
    }

    @Test
    void delete_ShouldReturn200_WhenSuccess() throws Exception {
        doNothing().when(genreService).delete(1L);
        mockMvc.perform(delete("/api/genres/1"))
                .andExpect(status().isOk());
        verify(genreService).delete(1L);
    }

    @Test
    void delete_ShouldReturn404_WhenGenreNotFound() throws Exception {
        doThrow(new GenreNotFoundException("Not found")).when(genreService).delete(2L);
        mockMvc.perform(delete("/api/genres/2"))
                .andExpect(status().is(500));
        verify(genreService).delete(2L);
    }
} 