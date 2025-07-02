package ru.alexandr.BookingCinemaTickets.application.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.alexandr.BookingCinemaTickets.application.dto.hall.HallCreateDto;
import ru.alexandr.BookingCinemaTickets.application.dto.hall.HallDto;
import ru.alexandr.BookingCinemaTickets.application.dto.hall.HallPreviewDto;
import ru.alexandr.BookingCinemaTickets.application.dto.hall.HallUpdateDto;
import ru.alexandr.BookingCinemaTickets.application.dto.seat.SeatDto;
import ru.alexandr.BookingCinemaTickets.application.exception.HallNotFoundException;
import ru.alexandr.BookingCinemaTickets.application.mapper.HallMapper;
import ru.alexandr.BookingCinemaTickets.domain.model.Hall;
import ru.alexandr.BookingCinemaTickets.infrastructure.repository.jpa.HallRepository;

import java.util.List;

@Service
public class HallService {
    private final HallRepository hallRepository;
    private final HallMapper hallMapper;
    private final SeatService seatService;

    public HallService(HallRepository hallRepository,
                       HallMapper hallMapper,
                       SeatService seatService) {
        this.hallRepository = hallRepository;
        this.hallMapper = hallMapper;
        this.seatService = seatService;
    }

    @Transactional
    public HallDto create(HallCreateDto hallCreateDto) {
        Hall hall = hallMapper.toEntity(hallCreateDto);
        hall = hallRepository.saveAndFlush(hall);
        List<SeatDto> seatDtos = seatService.create(hall.getId(), hallCreateDto.seats());
        return hallMapper.toDto(hall, seatDtos);
    }

    @Transactional(readOnly = true)
    public Page<HallPreviewDto> getAllPreview(Pageable pageable) {
        Page<Hall> halls = hallRepository.findAll(pageable);
        return hallMapper.toPreviewDto(halls);
    }

    @Transactional(readOnly = true)
    public HallDto get(Long id) {
        Hall hallWithSeats = hallRepository.findByIdWithSeats(id)
                .orElseThrow(() -> new HallNotFoundException(String.format("Зал с id: %s не найден", id)));
        return hallMapper.toDto(hallWithSeats);
    }

    @Transactional
    public HallPreviewDto update(Long id, HallUpdateDto hallUpdateDto) {
        Hall hall = hallRepository.findById(id)
                .orElseThrow(() -> new HallNotFoundException(String.format("Зал с id: %s не найден", id)));
        hall.update(hallUpdateDto);
        return hallMapper.toPreviewDto(hall);
    }

    @Transactional
    public void delete(Long id) {
        hallRepository.deleteById(id);
    }
}
