package ru.alexandr.BookingCinemaTickets.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.alexandr.BookingCinemaTickets.application.dto.sessionSeat.SessionSeatCreateDto;
import ru.alexandr.BookingCinemaTickets.application.dto.sessionSeat.SessionSeatDto;
import ru.alexandr.BookingCinemaTickets.application.dto.sessionSeat.SessionSeatUpdateDto;
import ru.alexandr.BookingCinemaTickets.application.exception.SeatNotFoundException;
import ru.alexandr.BookingCinemaTickets.application.exception.SessionNotFoundException;
import ru.alexandr.BookingCinemaTickets.application.exception.SessionSeatNotFoundException;
import ru.alexandr.BookingCinemaTickets.application.mapper.SessionSeatMapper;
import ru.alexandr.BookingCinemaTickets.domain.enums.SessionSeatStatus;
import ru.alexandr.BookingCinemaTickets.domain.model.Seat;
import ru.alexandr.BookingCinemaTickets.domain.model.Session;
import ru.alexandr.BookingCinemaTickets.domain.model.SessionSeat;
import ru.alexandr.BookingCinemaTickets.infrastructure.repository.jpa.SeatRepository;
import ru.alexandr.BookingCinemaTickets.infrastructure.repository.jpa.SessionRepository;
import ru.alexandr.BookingCinemaTickets.infrastructure.repository.jpa.SessionSeatRepository;

import java.util.List;

@Service
public class SessionSeatService {
    private final SessionSeatRepository sessionSeatRepository;
    private final SessionSeatMapper sessionSeatMapper;
    private final SessionRepository sessionRepository;
    private final SeatRepository seatRepository;

    public SessionSeatService(SessionSeatRepository sessionSeatRepository,
                              SessionSeatMapper sessionSeatMapper, SessionRepository sessionRepository, SeatRepository seatRepository) {
        this.sessionSeatRepository = sessionSeatRepository;
        this.sessionSeatMapper = sessionSeatMapper;
        this.sessionRepository = sessionRepository;
        this.seatRepository = seatRepository;
    }

    @Transactional
    public List<SessionSeatDto> create(Long sessionId, List<SessionSeatCreateDto> createDtos) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new SessionNotFoundException(String.format("Сессия с id %s не найдена", sessionId)));
        List<SessionSeat> sessionSeats = createDtos.stream().map(dto -> {
                    Seat seat = seatRepository.findById(dto.seatId())
                            .orElseThrow(() -> new SeatNotFoundException(String.format("Места в зале с id %s не найдено", dto.seatId())));
                    SessionSeat sessionSeat = sessionSeatMapper.toEntity(session, seat, dto);
                    return sessionSeatRepository.save(sessionSeat);
                }).toList();
        return sessionSeatMapper.toDto(sessionSeats);
    }

    @Transactional
    public SessionSeatDto update(Long sessionSeatId, Long sessionId, SessionSeatUpdateDto updateDto) {
        SessionSeat sessionSeat = sessionSeatRepository.findByIdAndSessionId(sessionSeatId, sessionId)
                .orElseThrow(() -> new SessionSeatNotFoundException(String.format("Места %s на киносеанс %s не найдено", sessionSeatId, sessionId)));
        sessionSeat.update(updateDto);
        return sessionSeatMapper.toDto(sessionSeat);
    }

    @Transactional
    public void delete(Long sessionSeatId, Long sessionId) {
        sessionSeatRepository.deleteByIdAndSessionId(sessionSeatId, sessionId);
    }

    @Transactional
    public void book(Long sessionSeatId) {
        SessionSeat sessionSeat = sessionSeatRepository.findById(sessionSeatId)
                .orElseThrow(() -> new SessionSeatNotFoundException("Место не найдено"));
        if (sessionSeat.getStatus() != SessionSeatStatus.AVAILABLE) {
            throw new IllegalStateException("Место уже занято");
        }
        sessionSeat.setStatus(SessionSeatStatus.BOOKED);
    }
}
