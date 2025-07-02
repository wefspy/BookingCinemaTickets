package ru.alexandr.BookingCinemaTickets.application.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.alexandr.BookingCinemaTickets.application.dto.sessionSeat.SessionSeatCreateDto;
import ru.alexandr.BookingCinemaTickets.application.dto.sessionSeat.SessionSeatDto;
import ru.alexandr.BookingCinemaTickets.application.mapper.SessionSeatMapper;
import ru.alexandr.BookingCinemaTickets.domain.model.SessionSeat;
import ru.alexandr.BookingCinemaTickets.infrastructure.repository.jpa.SessionSeatRepository;
import ru.alexandr.BookingCinemaTickets.infrastructure.repository.jpa.SessionRepository;
import ru.alexandr.BookingCinemaTickets.infrastructure.repository.jpa.SeatRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SessionSeatServiceUnitTest {
    @InjectMocks
    private SessionSeatService sessionSeatService;
    @Mock
    private SessionSeatRepository sessionSeatRepository;
    @Mock
    private SessionSeatMapper sessionSeatMapper;
    @Mock
    private SessionRepository sessionRepository;
    @Mock
    private SeatRepository seatRepository;

    @Test
    void book_ShouldChangeStatus_WhenAvailable() {
        SessionSeat sessionSeat = mock(SessionSeat.class);
        when(sessionSeatRepository.findById(1L)).thenReturn(Optional.of(sessionSeat));
        when(sessionSeat.getStatus()).thenReturn(ru.alexandr.BookingCinemaTickets.domain.enums.SessionSeatStatus.AVAILABLE);

        sessionSeatService.book(1L);

        verify(sessionSeat).setStatus(ru.alexandr.BookingCinemaTickets.domain.enums.SessionSeatStatus.BOOKED);
        verify(sessionSeatRepository).findById(1L);
    }

    @Test
    void book_ShouldThrowException_WhenNotAvailable() {
        SessionSeat sessionSeat = mock(SessionSeat.class);
        when(sessionSeatRepository.findById(1L)).thenReturn(Optional.of(sessionSeat));
        when(sessionSeat.getStatus()).thenReturn(ru.alexandr.BookingCinemaTickets.domain.enums.SessionSeatStatus.BOOKED);

        org.assertj.core.api.Assertions.assertThatThrownBy(() -> sessionSeatService.book(1L))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void create_ShouldReturnSessionSeatDtos_WhenSuccess() {
        List<ru.alexandr.BookingCinemaTickets.application.dto.sessionSeat.SessionSeatCreateDto> createDtos = List.of(mock(ru.alexandr.BookingCinemaTickets.application.dto.sessionSeat.SessionSeatCreateDto.class));
        ru.alexandr.BookingCinemaTickets.domain.model.Session session = mock(ru.alexandr.BookingCinemaTickets.domain.model.Session.class);
        ru.alexandr.BookingCinemaTickets.domain.model.Seat seat = mock(ru.alexandr.BookingCinemaTickets.domain.model.Seat.class);
        ru.alexandr.BookingCinemaTickets.domain.model.SessionSeat sessionSeat = mock(ru.alexandr.BookingCinemaTickets.domain.model.SessionSeat.class);
        List<ru.alexandr.BookingCinemaTickets.domain.model.SessionSeat> sessionSeats = List.of(sessionSeat);
        List<ru.alexandr.BookingCinemaTickets.application.dto.sessionSeat.SessionSeatDto> dtos = List.of(mock(ru.alexandr.BookingCinemaTickets.application.dto.sessionSeat.SessionSeatDto.class));
        when(sessionRepository.findById(1L)).thenReturn(java.util.Optional.of(session));
        when(seatRepository.findById(any())).thenReturn(java.util.Optional.of(seat));
        when(sessionSeatMapper.toEntity(eq(session), eq(seat), any())).thenReturn(sessionSeat);
        when(sessionSeatRepository.save(sessionSeat)).thenReturn(sessionSeat);
        when(sessionSeatMapper.toDto(sessionSeats)).thenReturn(dtos);
        var result = sessionSeatService.create(1L, createDtos);
        assertThat(result).isEqualTo(dtos);
        verify(sessionRepository).findById(1L);
        verify(seatRepository).findById(any());
        verify(sessionSeatRepository).save(sessionSeat);
        verify(sessionSeatMapper).toDto(sessionSeats);
    }

    @Test
    void update_ShouldReturnSessionSeatDto_WhenExists() {
        ru.alexandr.BookingCinemaTickets.domain.model.SessionSeat sessionSeat = mock(ru.alexandr.BookingCinemaTickets.domain.model.SessionSeat.class);
        ru.alexandr.BookingCinemaTickets.application.dto.sessionSeat.SessionSeatUpdateDto updateDto = mock(ru.alexandr.BookingCinemaTickets.application.dto.sessionSeat.SessionSeatUpdateDto.class);
        ru.alexandr.BookingCinemaTickets.application.dto.sessionSeat.SessionSeatDto dto = mock(ru.alexandr.BookingCinemaTickets.application.dto.sessionSeat.SessionSeatDto.class);
        when(sessionSeatRepository.findByIdAndSessionId(1L, 2L)).thenReturn(java.util.Optional.of(sessionSeat));
        when(sessionSeatMapper.toDto(sessionSeat)).thenReturn(dto);
        var result = sessionSeatService.update(1L, 2L, updateDto);
        assertThat(result).isEqualTo(dto);
        verify(sessionSeatRepository).findByIdAndSessionId(1L, 2L);
        verify(sessionSeatMapper).toDto(sessionSeat);
    }

    @Test
    void update_ShouldThrowException_WhenNotExists() {
        ru.alexandr.BookingCinemaTickets.application.dto.sessionSeat.SessionSeatUpdateDto updateDto = mock(ru.alexandr.BookingCinemaTickets.application.dto.sessionSeat.SessionSeatUpdateDto.class);
        when(sessionSeatRepository.findByIdAndSessionId(1L, 2L)).thenReturn(java.util.Optional.empty());
        org.assertj.core.api.Assertions.assertThatThrownBy(() -> sessionSeatService.update(1L, 2L, updateDto))
                .isInstanceOf(RuntimeException.class);
        verify(sessionSeatRepository).findByIdAndSessionId(1L, 2L);
    }

    @Test
    void delete_ShouldCallRepositoryDelete() {
        sessionSeatService.delete(1L, 2L);
        verify(sessionSeatRepository).deleteByIdAndSessionId(1L, 2L);
    }
} 