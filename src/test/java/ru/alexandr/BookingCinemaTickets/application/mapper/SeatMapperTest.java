package ru.alexandr.BookingCinemaTickets.application.mapper;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.alexandr.BookingCinemaTickets.application.dto.seat.SeatData;
import ru.alexandr.BookingCinemaTickets.application.dto.seat.SeatDto;
import ru.alexandr.BookingCinemaTickets.domain.enums.SeatType;
import ru.alexandr.BookingCinemaTickets.domain.enums.SoundSystem;
import ru.alexandr.BookingCinemaTickets.domain.model.Hall;
import ru.alexandr.BookingCinemaTickets.domain.model.Seat;
import ru.alexandr.BookingCinemaTickets.testUtils.factory.TestEntityFactory;

import java.util.List;

class SeatMapperTest {
    private SeatMapper seatMapper;
    private Hall hall;
    private Seat seat;
    private SeatData seatData;

    @BeforeEach
    void setUp() {
        seatMapper = new SeatMapper();
        
        hall = TestEntityFactory.hall(1L, "Test Hall", SoundSystem.DOLBY_ATMOS);

        seatData = new SeatDto(1L, 1, 1, SeatType.STANDARD);
        
        seat = TestEntityFactory.seat(1L, hall, seatData.rowNumber(), seatData.seatNumber(), seatData.type());
    }

    @Test
    void toEntity_WhenGivenSingleSeat_ShouldMapCorrectly() {
        Seat result = seatMapper.toEntity(hall, seatData);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getHall()).isEqualTo(hall);
        Assertions.assertThat(result.getRowNumber()).isEqualTo(seatData.rowNumber());
        Assertions.assertThat(result.getSeatNumber()).isEqualTo(seatData.seatNumber());
        Assertions.assertThat(result.getType()).isEqualTo(seatData.type());
    }

    @Test
    void toEntity_WhenGivenListOfSeats_ShouldMapCorrectly() {
        List<SeatData> seatsData = List.of(seatData);

        List<Seat> result = seatMapper.toEntity(hall, seatsData);

        Assertions.assertThat(result).hasSize(1);
        Seat mappedSeat = result.get(0);
        Assertions.assertThat(mappedSeat.getHall()).isEqualTo(hall);
        Assertions.assertThat(mappedSeat.getRowNumber()).isEqualTo(seatData.rowNumber());
        Assertions.assertThat(mappedSeat.getSeatNumber()).isEqualTo(seatData.seatNumber());
        Assertions.assertThat(mappedSeat.getType()).isEqualTo(seatData.type());
    }

    @Test
    void toDto_WhenGivenSingleSeat_ShouldMapCorrectly() {
        SeatDto result = seatMapper.toDto(seat);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.id()).isEqualTo(seat.getId());
        Assertions.assertThat(result.rowNumber()).isEqualTo(seat.getRowNumber());
        Assertions.assertThat(result.seatNumber()).isEqualTo(seat.getSeatNumber());
        Assertions.assertThat(result.type()).isEqualTo(seat.getType());
    }

    @Test
    void toDto_WhenGivenListOfSeats_ShouldMapCorrectly() {
        List<Seat> seats = List.of(seat);

        List<SeatDto> result = seatMapper.toDto(seats);

        Assertions.assertThat(result).hasSize(1);
        SeatDto mappedSeatDto = result.get(0);
        Assertions.assertThat(mappedSeatDto.id()).isEqualTo(seat.getId());
        Assertions.assertThat(mappedSeatDto.rowNumber()).isEqualTo(seat.getRowNumber());
        Assertions.assertThat(mappedSeatDto.seatNumber()).isEqualTo(seat.getSeatNumber());
        Assertions.assertThat(mappedSeatDto.type()).isEqualTo(seat.getType());
    }
} 