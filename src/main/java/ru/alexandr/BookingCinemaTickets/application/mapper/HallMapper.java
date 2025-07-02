package ru.alexandr.BookingCinemaTickets.application.mapper;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import ru.alexandr.BookingCinemaTickets.application.dto.hall.HallData;
import ru.alexandr.BookingCinemaTickets.application.dto.hall.HallDto;
import ru.alexandr.BookingCinemaTickets.application.dto.hall.HallPreviewDto;
import ru.alexandr.BookingCinemaTickets.application.dto.seat.SeatDto;
import ru.alexandr.BookingCinemaTickets.domain.model.Hall;

import java.util.List;

@Component
public class HallMapper {
    private final SeatMapper seatMapper;

    public HallMapper(SeatMapper seatMapper) {
        this.seatMapper = seatMapper;
    }

    public Hall toEntity(HallData data) {
        return new Hall(data.name(), data.soundSystem());
    }

    public HallDto toDto(Hall hallWithSeats) {
        List<SeatDto> seatDtos = seatMapper.toDto(hallWithSeats.getSeats());
        return toDto(hallWithSeats, seatDtos);
    }

    public HallDto toDto(Hall hall, List<SeatDto> seatDtos) {
        return new HallDto(
                hall.getId(),
                hall.getName(),
                hall.getSoundSystem(),
                seatDtos
        );
    }

    public HallPreviewDto toPreviewDto(Hall hall) {
        return new HallPreviewDto(
                hall.getId(),
                hall.getName(),
                hall.getSoundSystem()
        );
    }

    public Page<HallPreviewDto> toPreviewDto(Page<Hall> halls) {
        return halls.map(this::toPreviewDto);
    }
}
