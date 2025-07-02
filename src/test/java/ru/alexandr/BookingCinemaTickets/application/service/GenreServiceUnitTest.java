package ru.alexandr.BookingCinemaTickets.application.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.alexandr.BookingCinemaTickets.application.dto.genre.GenreCreateDto;
import ru.alexandr.BookingCinemaTickets.application.dto.genre.GenreDto;
import ru.alexandr.BookingCinemaTickets.application.mapper.GenreMapper;
import ru.alexandr.BookingCinemaTickets.domain.model.Genre;
import ru.alexandr.BookingCinemaTickets.infrastructure.repository.jpa.GenreRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GenreServiceUnitTest {
    @InjectMocks
    private GenreService genreService;
    @Mock
    private GenreRepository genreRepository;
    @Mock
    private GenreMapper genreMapper;

    @Test
    void create_ShouldReturnGenreDto_WhenSuccess() {
        GenreCreateDto createDto = new GenreCreateDto("Test Genre", "desc");
        Genre genre = new Genre("Test Genre", "desc");
        GenreDto genreDto = new GenreDto(1L, "Test Genre", "desc");

        when(genreMapper.toEntity(createDto)).thenReturn(genre);
        when(genreRepository.save(genre)).thenReturn(genre);
        when(genreMapper.toDto(genre)).thenReturn(genreDto);

        GenreDto result = genreService.create(createDto);

        assertThat(result).isEqualTo(genreDto);
        verify(genreRepository).save(genre);
    }

    @Test
    void update_ShouldReturnGenreDto_WhenExists() {
        Genre genre = new Genre("Test Genre", "desc");
        GenreDto genreDto = new GenreDto(1L, "Test Genre", "desc");
        GenreCreateDto updateDto = new GenreCreateDto("Test Genre", "desc");
        when(genreRepository.findById(1L)).thenReturn(Optional.of(genre));
        when(genreMapper.toDto(genre)).thenReturn(genreDto);
        GenreDto result = genreService.update(1L, updateDto);
        assertThat(result).isEqualTo(genreDto);
        verify(genreRepository).findById(1L);
        verify(genreMapper).toDto(genre);
    }

    @Test
    void update_ShouldThrowException_WhenNotExists() {
        GenreCreateDto updateDto = new GenreCreateDto("Test Genre", "desc");
        when(genreRepository.findById(1L)).thenReturn(Optional.empty());
        org.assertj.core.api.Assertions.assertThatThrownBy(() -> genreService.update(1L, updateDto))
                .isInstanceOf(RuntimeException.class);
        verify(genreRepository).findById(1L);
    }

    @Test
    void delete_ShouldCallRepositoryDelete() {
        genreService.delete(1L);
        verify(genreRepository).deleteById(1L);
    }

    @Test
    void getAll_ShouldReturnPage() {
        Page<Genre> page = mock(Page.class);
        Pageable pageable = mock(Pageable.class);
        Page<GenreDto> dtoPage = mock(Page.class);
        when(genreRepository.findAll(pageable)).thenReturn(page);
        when(genreMapper.toDto(page)).thenReturn(dtoPage);
        var result = genreService.getAll(pageable);
        assertThat(result).isEqualTo(dtoPage);
        verify(genreRepository).findAll(pageable);
        verify(genreMapper).toDto(page);
    }
} 