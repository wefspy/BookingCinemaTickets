package ru.alexandr.BookingCinemaTickets.application.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.alexandr.BookingCinemaTickets.application.dto.session.SessionCreateDto;
import ru.alexandr.BookingCinemaTickets.application.dto.session.SessionDto;
import ru.alexandr.BookingCinemaTickets.application.dto.session.SessionUpdateDto;
import ru.alexandr.BookingCinemaTickets.application.dto.session.SessionPreviewDto;
import ru.alexandr.BookingCinemaTickets.application.mapper.SessionMapper;
import ru.alexandr.BookingCinemaTickets.domain.model.Session;
import ru.alexandr.BookingCinemaTickets.infrastructure.repository.jpa.HallRepository;
import ru.alexandr.BookingCinemaTickets.infrastructure.repository.jpa.MovieRepository;
import ru.alexandr.BookingCinemaTickets.infrastructure.repository.jpa.SessionRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SessionServiceUnitTest {
    @InjectMocks
    private SessionService sessionService;
    @Mock
    private SessionRepository sessionRepository;
    @Mock
    private SessionMapper sessionMapper;
    @Mock
    private HallRepository hallRepository;
    @Mock
    private MovieRepository movieRepository;
    @Mock
    private SessionSeatService sessionSeatService;

    @Test
    void getById_ShouldReturnSessionDto_WhenExists() {
        Session session = mock(Session.class);
        SessionDto sessionDto = mock(SessionDto.class);

        when(sessionRepository.findByIdWithHallAndMovieAndSessionSeat(1L)).thenReturn(Optional.of(session));
        when(sessionMapper.toDto(session)).thenReturn(sessionDto);

        SessionDto result = sessionService.getById(1L);

        assertThat(result).isEqualTo(sessionDto);
        verify(sessionRepository).findByIdWithHallAndMovieAndSessionSeat(1L);
    }

    @Test
    void getById_ShouldThrowException_WhenNotExists() {
        when(sessionRepository.findByIdWithHallAndMovieAndSessionSeat(1L)).thenReturn(Optional.empty());

        org.assertj.core.api.Assertions.assertThatThrownBy(() -> sessionService.getById(1L))
                .isInstanceOf(RuntimeException.class);

        verify(sessionRepository).findByIdWithHallAndMovieAndSessionSeat(1L);
    }

    @Test
    void update_ShouldThrowException_WhenNotExists() {
        SessionUpdateDto updateDto = mock(SessionUpdateDto.class);
        when(sessionRepository.findByIdWithHallAndMovie(1L)).thenReturn(Optional.empty());
        org.assertj.core.api.Assertions.assertThatThrownBy(() -> sessionService.update(1L, updateDto))
                .isInstanceOf(RuntimeException.class);
        verify(sessionRepository).findByIdWithHallAndMovie(1L);
    }

    @Test
    void delete_ShouldCallRepositoryDelete() {
        sessionService.delete(1L);
        verify(sessionRepository).deleteById(1L);
    }

    @Test
    void getAllByPage_ShouldReturnPage() {
        Page<Session> page = mock(Page.class);
        Pageable pageable = mock(Pageable.class);
        Page<SessionPreviewDto> dtoPage = mock(Page.class);
        when(sessionRepository.findAll(pageable)).thenReturn(page);
        when(sessionMapper.toPreviewDto(page)).thenReturn(dtoPage);
        var result = sessionService.getAllByPage(pageable);
        assertThat(result).isEqualTo(dtoPage);
        verify(sessionRepository).findAll(pageable);
        verify(sessionMapper).toPreviewDto(page);
    }

    @Test
    void getAllByHallId_ShouldReturnPage() {
        Page<Session> page = mock(Page.class);
        Pageable pageable = mock(Pageable.class);
        Page<SessionPreviewDto> dtoPage = mock(Page.class);
        when(sessionRepository.findAllByHallId(1L, pageable)).thenReturn(page);
        when(sessionMapper.toPreviewDto(page)).thenReturn(dtoPage);
        var result = sessionService.getAllByHallId(1L, pageable);
        assertThat(result).isEqualTo(dtoPage);
        verify(sessionRepository).findAllByHallId(1L, pageable);
        verify(sessionMapper).toPreviewDto(page);
    }

    @Test
    void getAllByMovieId_ShouldReturnPage() {
        Page<Session> page = mock(Page.class);
        Pageable pageable = mock(Pageable.class);
        Page<SessionPreviewDto> dtoPage = mock(Page.class);
        when(sessionRepository.findAllByMovieId(1L, pageable)).thenReturn(page);
        when(sessionMapper.toPreviewDto(page)).thenReturn(dtoPage);
        var result = sessionService.getAllByMovieId(1L, pageable);
        assertThat(result).isEqualTo(dtoPage);
        verify(sessionRepository).findAllByMovieId(1L, pageable);
        verify(sessionMapper).toPreviewDto(page);
    }
} 