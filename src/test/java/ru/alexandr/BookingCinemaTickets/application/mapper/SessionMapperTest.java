package ru.alexandr.BookingCinemaTickets.application.mapper;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import ru.alexandr.BookingCinemaTickets.application.dto.hall.HallDto;
import ru.alexandr.BookingCinemaTickets.application.dto.movie.MovieDto;
import ru.alexandr.BookingCinemaTickets.application.dto.seat.SeatDto;
import ru.alexandr.BookingCinemaTickets.application.dto.session.SessionData;
import ru.alexandr.BookingCinemaTickets.application.dto.session.SessionDto;
import ru.alexandr.BookingCinemaTickets.application.dto.session.SessionPreviewDto;
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
class SessionMapperTest {
    @Mock
    private MovieMapper movieMapper;
    @Mock
    private HallMapper hallMapper;
    @Mock
    private SessionSeatMapper sessionSeatMapper;

    private SessionMapper sessionMapper;
    private Session session;
    private SessionData sessionData;
    private Movie movie;
    private Hall hall;
    private MovieDto movieDto;
    private HallDto hallDto;
    private SessionSeat sessionSeat;
    private SessionSeatDto sessionSeatDto;
    private SeatDto seatDto;

    @BeforeEach
    void setUp() {
        sessionMapper = new SessionMapper(movieMapper, hallMapper, sessionSeatMapper);

        LocalDateTime startTime = LocalDateTime.now();
        
        movie = TestEntityFactory.movie(1L, "Test Movie", "Description", 120, LocalDate.now(), Rating.G);
        hall = TestEntityFactory.hall(1L, "Test Hall", SoundSystem.DOLBY_ATMOS);
        session = TestEntityFactory.session(1L, movie, hall, startTime);

        movieDto = new MovieDto(1L, "Test Movie", "Description", 120, LocalDate.now(), Rating.G, List.of());
        hallDto = new HallDto(1L, "Test Hall", SoundSystem.DOLBY_ATMOS, List.of());

        sessionData = new SessionPreviewDto(1L, movieDto, hallDto, startTime);
        seatDto = new SeatDto(1L, 1, 1, SeatType.STANDARD);

        Seat seat = TestEntityFactory.seat(1L, hall, 1, 1, SeatType.STANDARD);
        sessionSeat = TestEntityFactory.sessionSeat(1L, session, seat, BigDecimal.TEN, SessionSeatStatus.AVAILABLE);
        sessionSeatDto = new SessionSeatDto(1L, BigDecimal.TEN, SessionSeatStatus.AVAILABLE, seatDto);
        
        session.getSessionSeat().add(sessionSeat);
    }

    @Test
    void toEntity_ShouldMapCorrectly() {
        Session result = sessionMapper.toEntity(hall, movie, sessionData);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getMovie()).isEqualTo(movie);
        Assertions.assertThat(result.getHall()).isEqualTo(hall);
        Assertions.assertThat(result.getStartTime()).isEqualTo(sessionData.startTime());
    }

    @Test
    void toDto_WhenGivenSession_ShouldMapCorrectly() {
        // Setup mock behavior
        Mockito.when(movieMapper.toDto(movie)).thenReturn(movieDto);
        Mockito.when(hallMapper.toDto(hall)).thenReturn(hallDto);
        Mockito.when(sessionSeatMapper.toDto(sessionSeat)).thenReturn(sessionSeatDto);

        SessionDto result = sessionMapper.toDto(session);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.id()).isEqualTo(session.getId());
        Assertions.assertThat(result.movie()).isEqualTo(movieDto);
        Assertions.assertThat(result.hall()).isEqualTo(hallDto);
        Assertions.assertThat(result.startTime()).isEqualTo(session.getStartTime());
    }

    @Test
    void toDto_WhenGivenSessionAndSeats_ShouldMapCorrectly() {
        // Setup mock behavior
        Mockito.when(movieMapper.toDto(movie)).thenReturn(movieDto);
        Mockito.when(hallMapper.toDto(hall)).thenReturn(hallDto);

        SessionDto result = sessionMapper.toDto(session, List.of(sessionSeatDto));

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.id()).isEqualTo(session.getId());
        Assertions.assertThat(result.movie()).isEqualTo(movieDto);
        Assertions.assertThat(result.hall()).isEqualTo(hallDto);
        Assertions.assertThat(result.startTime()).isEqualTo(session.getStartTime());
    }

    @Test
    void toPreviewDto_WhenGivenSession_ShouldMapCorrectly() {
        // Setup mock behavior
        Mockito.when(movieMapper.toDto(movie)).thenReturn(movieDto);
        Mockito.when(hallMapper.toDto(hall)).thenReturn(hallDto);

        SessionPreviewDto result = sessionMapper.toPreviewDto(session);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.id()).isEqualTo(session.getId());
        Assertions.assertThat(result.movie()).isEqualTo(movieDto);
        Assertions.assertThat(result.hall()).isEqualTo(hallDto);
        Assertions.assertThat(result.startTime()).isEqualTo(session.getStartTime());
    }

    @Test
    void toPreviewDto_WhenGivenPageOfSessions_ShouldMapCorrectly() {
        // Setup mock behavior
        Mockito.when(movieMapper.toDto(movie)).thenReturn(movieDto);
        Mockito.when(hallMapper.toDto(hall)).thenReturn(hallDto);
        Page<Session> sessionPage = new PageImpl<>(List.of(session));

        Page<SessionPreviewDto> result = sessionMapper.toPreviewDto(sessionPage);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getContent()).hasSize(1);
        SessionPreviewDto sessionPreviewDto = result.getContent().get(0);
        Assertions.assertThat(sessionPreviewDto.id()).isEqualTo(session.getId());
        Assertions.assertThat(sessionPreviewDto.movie()).isEqualTo(movieDto);
        Assertions.assertThat(sessionPreviewDto.hall()).isEqualTo(hallDto);
        Assertions.assertThat(sessionPreviewDto.startTime()).isEqualTo(session.getStartTime());
    }
} 