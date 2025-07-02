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
import ru.alexandr.BookingCinemaTickets.application.dto.movie.MovieCreateDto;
import ru.alexandr.BookingCinemaTickets.application.dto.movie.MovieDto;
import ru.alexandr.BookingCinemaTickets.application.exception.MovieNotFoundException;
import ru.alexandr.BookingCinemaTickets.application.service.MovieService;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = MovieRestController.class)
@AutoConfigureMockMvc(addFilters = false)
public class MovieRestControllerUnitTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockitoBean
    private MovieService movieService;

    @Test
    void create_ShouldReturn201_WhenSuccess() throws Exception {
        MovieCreateDto createDto = mock(MovieCreateDto.class);
        MovieDto movieDto = mock(MovieDto.class);
        when(movieService.create(any(MovieCreateDto.class))).thenReturn(movieDto);
        mockMvc.perform(post("/api/movies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isCreated());
        verify(movieService).create(any(MovieCreateDto.class));
    }

    @Test
    void getAll_ShouldReturn200() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        when(movieService.getAll(any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.emptyList(), pageable, 0));
        mockMvc.perform(get("/api/movies"))
                .andExpect(status().isOk());
        verify(movieService).getAll(any(Pageable.class));
    }

    @Test
    void update_ShouldReturn200_WhenSuccess() throws Exception {
        MovieDto movieDto = mock(MovieDto.class);
        when(movieService.update(eq(1L), any(MovieCreateDto.class))).thenReturn(movieDto);
        MovieCreateDto updateDto = mock(MovieCreateDto.class);
        mockMvc.perform(put("/api/movies/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk());
        verify(movieService).update(eq(1L), any(MovieCreateDto.class));
    }

    @Test
    void update_ShouldReturn404_WhenMovieNotFound() throws Exception {
        when(movieService.update(eq(2L), any(MovieCreateDto.class)))
                .thenThrow(new MovieNotFoundException("Not found"));
        MovieCreateDto updateDto = mock(MovieCreateDto.class);
        mockMvc.perform(put("/api/movies/2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().is(500));
        verify(movieService).update(eq(2L), any(MovieCreateDto.class));
    }

    @Test
    void delete_ShouldReturn200_WhenSuccess() throws Exception {
        doNothing().when(movieService).delete(1L);
        mockMvc.perform(delete("/api/movies/1"))
                .andExpect(status().isOk());
        verify(movieService).delete(1L);
    }

    @Test
    void delete_ShouldReturn404_WhenMovieNotFound() throws Exception {
        doThrow(new MovieNotFoundException("Not found")).when(movieService).delete(2L);
        mockMvc.perform(delete("/api/movies/2"))
                .andExpect(status().is(500));
        verify(movieService).delete(2L);
    }
} 