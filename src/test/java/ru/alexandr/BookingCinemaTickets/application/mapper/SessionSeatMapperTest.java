package ru.alexandr.BookingCinemaTickets.application.mapper;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.alexandr.BookingCinemaTickets.application.dto.seat.SeatDto;
import ru.alexandr.BookingCinemaTickets.application.dto.sessionSeat.SessionSeatCreateDto;
import ru.alexandr.BookingCinemaTickets.application.dto.sessionSeat.SessionSeatDto;
import ru.alexandr.BookingCinemaTickets.domain.enums.Rating;
import ru.alexandr.BookingCinemaTickets.domain.enums.SeatType;
import ru.alexandr.BookingCinemaTickets.domain.enums.SessionSeatStatus;
import ru.alexandr.BookingCinemaTickets.domain.enums.SoundSystem;
import ru.alexandr.BookingCinemaTickets.domain.model.*;
import ru.alexandr.BookingCinemaTickets.testUtils.factory.TestEntityFactory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class SessionSeatMapperTest {
    @Mock
    private SeatMapper seatMapper;

    private SessionSeatMapper sessionSeatMapper;
    private Session session;
    private Seat seat;
    private SessionSeat sessionSeat;
    private SessionSeatCreateDto sessionSeatCreateDto;
    private SeatDto seatDto;

    @BeforeEach
    void setUp() {
        sessionSeatMapper = new SessionSeatMapper(seatMapper);
        
        Movie movie = TestEntityFactory.movie(1L, "Test Movie", "Description", 120, LocalDate.now(), Rating.G);
        Hall hall = TestEntityFactory.hall(1L, "Test Hall", SoundSystem.DOLBY_ATMOS);
        session = TestEntityFactory.session(1L, movie, hall, LocalDateTime.now());
        
        seat = TestEntityFactory.seat(1L, hall, 1, 1, SeatType.STANDARD);
        seatDto = new SeatDto(1L, 1, 1, SeatType.STANDARD);
        
        sessionSeatCreateDto = new SessionSeatCreateDto(1L, BigDecimal.TEN, SessionSeatStatus.AVAILABLE);
        sessionSeat = TestEntityFactory.sessionSeat(1L, session, seat, BigDecimal.TEN, SessionSeatStatus.AVAILABLE);
    }

    @Test
    void toEntity_ShouldMapCorrectly() {
        SessionSeat result = sessionSeatMapper.toEntity(session, seat, sessionSeatCreateDto);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getSession()).isEqualTo(session);
        Assertions.assertThat(result.getSeat()).isEqualTo(seat);
        Assertions.assertThat(result.getPrice()).isEqualTo(sessionSeatCreateDto.price());
        Assertions.assertThat(result.getStatus()).isEqualTo(sessionSeatCreateDto.status());
    }

    @Test
    void toDto_WhenGivenSingleSessionSeat_ShouldMapCorrectly() {
        Mockito.when(seatMapper.toDto(seat)).thenReturn(seatDto);

        SessionSeatDto result = sessionSeatMapper.toDto(sessionSeat);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.id()).isEqualTo(sessionSeat.getId());
        Assertions.assertThat(result.price()).isEqualTo(sessionSeat.getPrice());
        Assertions.assertThat(result.status()).isEqualTo(sessionSeat.getStatus());
        Assertions.assertThat(result.seat()).isEqualTo(seatDto);
    }

    @Test
    void toDto_WhenGivenListOfSessionSeats_ShouldMapCorrectly() {
        Mockito.when(seatMapper.toDto(seat)).thenReturn(seatDto);
        List<SessionSeat> sessionSeats = List.of(sessionSeat);

        List<SessionSeatDto> result = sessionSeatMapper.toDto(sessionSeats);

        Assertions.assertThat(result).hasSize(1);
        SessionSeatDto mappedSessionSeatDto = result.get(0);
        Assertions.assertThat(mappedSessionSeatDto.id()).isEqualTo(sessionSeat.getId());
        Assertions.assertThat(mappedSessionSeatDto.price()).isEqualTo(sessionSeat.getPrice());
        Assertions.assertThat(mappedSessionSeatDto.status()).isEqualTo(sessionSeat.getStatus());
        Assertions.assertThat(mappedSessionSeatDto.seat()).isEqualTo(seatDto);
    }
} 