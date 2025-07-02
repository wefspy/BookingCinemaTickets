package ru.alexandr.BookingCinemaTickets.application.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.alexandr.BookingCinemaTickets.application.dto.session.SessionCreateDto;
import ru.alexandr.BookingCinemaTickets.application.dto.session.SessionDto;
import ru.alexandr.BookingCinemaTickets.application.dto.session.SessionPreviewDto;
import ru.alexandr.BookingCinemaTickets.application.dto.session.SessionUpdateDto;
import ru.alexandr.BookingCinemaTickets.application.dto.sessionSeat.SessionSeatDto;
import ru.alexandr.BookingCinemaTickets.application.exception.HallNotFoundException;
import ru.alexandr.BookingCinemaTickets.application.exception.MovieNotFoundException;
import ru.alexandr.BookingCinemaTickets.application.exception.SessionNotFoundException;
import ru.alexandr.BookingCinemaTickets.application.mapper.SessionMapper;
import ru.alexandr.BookingCinemaTickets.domain.model.Hall;
import ru.alexandr.BookingCinemaTickets.domain.model.Movie;
import ru.alexandr.BookingCinemaTickets.domain.model.Session;
import ru.alexandr.BookingCinemaTickets.infrastructure.repository.jpa.HallRepository;
import ru.alexandr.BookingCinemaTickets.infrastructure.repository.jpa.MovieRepository;
import ru.alexandr.BookingCinemaTickets.infrastructure.repository.jpa.SessionRepository;

import java.util.List;

@Service
public class SessionService {
    private final SessionRepository sessionRepository;
    private final SessionMapper sessionMapper;
    private final HallRepository hallRepository;
    private final MovieRepository movieRepository;
    private final SessionSeatService sessionSeatService;

    public SessionService(SessionRepository sessionRepository,
                          SessionMapper sessionMapper,
                          HallRepository hallRepository,
                          MovieRepository movieRepository,
                          SessionSeatService sessionSeatService) {
        this.sessionRepository = sessionRepository;
        this.sessionMapper = sessionMapper;
        this.hallRepository = hallRepository;
        this.movieRepository = movieRepository;
        this.sessionSeatService = sessionSeatService;
    }

    @Transactional
    public SessionDto create(SessionCreateDto sessionCreateDto) {
        Hall hall = hallRepository.findById(sessionCreateDto.hallId())
                .orElseThrow(() -> new HallNotFoundException(String.format("Зал с id %s не найден", sessionCreateDto.hallId())));
        Movie movie = movieRepository.findById(sessionCreateDto.movieId())
                .orElseThrow(() -> new MovieNotFoundException(String.format("Фильм с id %s не найден", sessionCreateDto.movieId())));
        Session session = sessionMapper.toEntity(hall, movie, sessionCreateDto);
        session = sessionRepository.saveAndFlush(session);
        List<SessionSeatDto> sessionSeats = sessionSeatService.create(session.getId(), sessionCreateDto.seats());
        return sessionMapper.toDto(session, sessionSeats);
    }

    @Transactional(readOnly = true)
    public Page<SessionPreviewDto> getAllByHallId(Long hallId, Pageable pageable) {
        Page<Session> sessions = sessionRepository.findAllByHallId(hallId, pageable);
        return sessionMapper.toPreviewDto(sessions);
    }

    @Transactional(readOnly = true)
    public Page<SessionPreviewDto> getAllByMovieId(Long movieId, Pageable pageable) {
        Page<Session> sessions = sessionRepository.findAllByMovieId(movieId, pageable);
        return sessionMapper.toPreviewDto(sessions);
    }

    @Transactional(readOnly = true)
    public SessionDto getById(Long id) {
        Session session = sessionRepository.findByIdWithHallAndMovieAndSessionSeat(id)
                .orElseThrow(() -> new SessionNotFoundException(String.format("Сеанс с id %s не найден", id)));
        return sessionMapper.toDto(session);
    }

    @Transactional
    public SessionPreviewDto update(Long id, SessionUpdateDto sessionUpdateDto) {
        Session session = sessionRepository.findByIdWithHallAndMovie(id)
                .orElseThrow(() -> new SessionNotFoundException(String.format("Сеанс с id %s не найден", id)));
        Hall hall = hallRepository.findById(sessionUpdateDto.hallId()).
                orElseThrow(() -> new HallNotFoundException(String.format("Зал с id %s не найден", sessionUpdateDto.hallId())));
        Movie movie = movieRepository.findById(sessionUpdateDto.movieId())
                .orElseThrow(() -> new MovieNotFoundException(String.format("Фильм с id %s не найден", sessionUpdateDto.movieId())));
        session.updateDependencies(hall, movie);
        return sessionMapper.toPreviewDto(session);
    }

    @Transactional
    public void delete(Long id) {
        sessionRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<SessionPreviewDto> getAllByPage(Pageable pageable) {
        Page<Session> sessions = sessionRepository.findAll(pageable);
        return sessionMapper.toPreviewDto(sessions);
    }
}
