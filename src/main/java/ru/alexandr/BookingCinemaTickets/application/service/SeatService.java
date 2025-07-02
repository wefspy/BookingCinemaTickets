package ru.alexandr.BookingCinemaTickets.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.alexandr.BookingCinemaTickets.application.dto.seat.SeatCreateDto;
import ru.alexandr.BookingCinemaTickets.application.dto.seat.SeatDto;
import ru.alexandr.BookingCinemaTickets.application.exception.HallNotFoundException;
import ru.alexandr.BookingCinemaTickets.application.exception.SeatNotFoundException;
import ru.alexandr.BookingCinemaTickets.application.mapper.SeatMapper;
import ru.alexandr.BookingCinemaTickets.domain.model.Hall;
import ru.alexandr.BookingCinemaTickets.domain.model.Seat;
import ru.alexandr.BookingCinemaTickets.infrastructure.repository.jpa.HallRepository;
import ru.alexandr.BookingCinemaTickets.infrastructure.repository.jpa.SeatRepository;

import java.util.List;
import java.util.Optional;

@Service
public class SeatService {
    private final SeatRepository seatRepository;
    private final SeatMapper seatMapper;
    private final HallRepository hallRepository;

    public SeatService(SeatRepository seatRepository, SeatMapper seatMapper, HallRepository hallRepository) {
        this.seatRepository = seatRepository;
        this.seatMapper = seatMapper;
        this.hallRepository = hallRepository;
    }

    @Transactional
    public List<SeatDto> create(Long hallId, List<SeatCreateDto> seatDtos) {
        Hall hall = hallRepository.findById(hallId)
                .orElseThrow(() -> new HallNotFoundException(String.format("Зал с id %s не найден", hallId)));
        List<Seat> seatEntities =  seatMapper.toEntity(hall, seatDtos);
        seatEntities = seatRepository.saveAll(seatEntities);
        return seatMapper.toDto(seatEntities);
    }

    @Transactional
    public SeatDto update(Long hallId, Long seatId, SeatCreateDto seatCreateDto) {
        Seat seat = seatRepository.findByIdAndHallId(seatId, hallId)
                .orElseThrow(() -> new SeatNotFoundException(String.format("Место %s не найдено в зале %s", seatId, hallId)));
        seat.update(seatCreateDto);
        return seatMapper.toDto(seat);
    }

    @Transactional
    public void delete(Long hallId, Long seatId) {
        Seat seat = seatRepository.findByIdAndHallId(seatId, hallId)
                .orElseThrow(() -> new SeatNotFoundException(String.format("Место %s не найдено в зале %s", seatId, hallId)));
        seatRepository.deleteById(seatId);
    }
}
