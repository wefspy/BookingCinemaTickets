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
import ru.alexandr.BookingCinemaTickets.application.dto.hall.HallData;
import ru.alexandr.BookingCinemaTickets.application.dto.hall.HallDto;
import ru.alexandr.BookingCinemaTickets.application.dto.hall.HallPreviewDto;
import ru.alexandr.BookingCinemaTickets.application.dto.seat.SeatDto;
import ru.alexandr.BookingCinemaTickets.domain.enums.SeatType;
import ru.alexandr.BookingCinemaTickets.domain.enums.SoundSystem;
import ru.alexandr.BookingCinemaTickets.domain.model.Hall;
import ru.alexandr.BookingCinemaTickets.domain.model.Seat;
import ru.alexandr.BookingCinemaTickets.testUtils.factory.TestEntityFactory;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class HallMapperTest {
    @Mock
    private SeatMapper seatMapper;

    private HallMapper hallMapper;
    private Hall hall;
    private HallData hallData;
    private Seat seat;
    private SeatDto seatDto;

    @BeforeEach
    void setUp() {
        hallMapper = new HallMapper(seatMapper);

        hallData = new HallPreviewDto(
                1L,
                "Test Hall",
                SoundSystem.DOLBY_ATMOS
        );

        hall = TestEntityFactory.hall(
                1L,
                hallData.name(),
                hallData.soundSystem()
        );

        seat = TestEntityFactory.seat(1L, hall, 1, 1, SeatType.STANDARD);
        seatDto = new SeatDto(1L, 1, 1, SeatType.STANDARD);

        hall.getSeats().add(seat);
    }

    @Test
    void toEntity_ShouldMapCorrectly() {
        Hall result = hallMapper.toEntity(hallData);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getName()).isEqualTo(hallData.name());
        Assertions.assertThat(result.getSoundSystem()).isEqualTo(hallData.soundSystem());
    }

    @Test
    void toDto_WhenGivenHall_ShouldMapCorrectly() {
        Mockito.when(seatMapper.toDto(hall.getSeats())).thenReturn(List.of(seatDto));

        HallDto result = hallMapper.toDto(hall);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.id()).isEqualTo(hall.getId());
        Assertions.assertThat(result.name()).isEqualTo(hall.getName());
        Assertions.assertThat(result.soundSystem()).isEqualTo(hall.getSoundSystem());
        Assertions.assertThat(result.seats()).hasSize(1);
        Assertions.assertThat(result.seats().get(0)).isEqualTo(seatDto);
    }

    @Test
    void toDto_WhenGivenHallAndSeats_ShouldMapCorrectly() {
        HallDto result = hallMapper.toDto(hall, List.of(seatDto));

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.id()).isEqualTo(hall.getId());
        Assertions.assertThat(result.name()).isEqualTo(hall.getName());
        Assertions.assertThat(result.soundSystem()).isEqualTo(hall.getSoundSystem());
        Assertions.assertThat(result.seats()).hasSize(1);
        Assertions.assertThat(result.seats().get(0)).isEqualTo(seatDto);
    }

    @Test
    void toPreviewDto_WhenGivenHall_ShouldMapCorrectly() {
        HallPreviewDto result = hallMapper.toPreviewDto(hall);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.id()).isEqualTo(hall.getId());
        Assertions.assertThat(result.name()).isEqualTo(hall.getName());
        Assertions.assertThat(result.soundSystem()).isEqualTo(hall.getSoundSystem());
    }

    @Test
    void toPreviewDto_WhenGivenPageOfHalls_ShouldMapCorrectly() {
        Page<Hall> hallPage = new PageImpl<>(List.of(hall));

        Page<HallPreviewDto> result = hallMapper.toPreviewDto(hallPage);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getContent()).hasSize(1);
        HallPreviewDto hallPreviewDto = result.getContent().get(0);
        Assertions.assertThat(hallPreviewDto.id()).isEqualTo(hall.getId());
        Assertions.assertThat(hallPreviewDto.name()).isEqualTo(hall.getName());
        Assertions.assertThat(hallPreviewDto.soundSystem()).isEqualTo(hall.getSoundSystem());
    }
} 