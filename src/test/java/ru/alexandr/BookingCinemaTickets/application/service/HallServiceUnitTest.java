package ru.alexandr.BookingCinemaTickets.application.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.alexandr.BookingCinemaTickets.application.dto.hall.HallCreateDto;
import ru.alexandr.BookingCinemaTickets.application.dto.hall.HallDto;
import ru.alexandr.BookingCinemaTickets.application.dto.hall.HallPreviewDto;
import ru.alexandr.BookingCinemaTickets.application.dto.hall.HallUpdateDto;
import ru.alexandr.BookingCinemaTickets.application.mapper.HallMapper;
import ru.alexandr.BookingCinemaTickets.domain.enums.SoundSystem;
import ru.alexandr.BookingCinemaTickets.domain.model.Hall;
import ru.alexandr.BookingCinemaTickets.infrastructure.repository.jpa.HallRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HallServiceUnitTest {
    @InjectMocks
    private HallService hallService;
    @Mock
    private HallRepository hallRepository;
    @Mock
    private HallMapper hallMapper;

    @Test
    void getAllPreview_ShouldReturnPage() {
        org.springframework.data.domain.Page<ru.alexandr.BookingCinemaTickets.domain.model.Hall> page = mock(org.springframework.data.domain.Page.class);
        org.springframework.data.domain.Pageable pageable = mock(org.springframework.data.domain.Pageable.class);
        org.springframework.data.domain.Page<ru.alexandr.BookingCinemaTickets.application.dto.hall.HallPreviewDto> previewPage = mock(org.springframework.data.domain.Page.class);
        when(hallRepository.findAll(pageable)).thenReturn(page);
        when(hallMapper.toPreviewDto(page)).thenReturn(previewPage);
        var result = hallService.getAllPreview(pageable);
        assertThat(result).isEqualTo(previewPage);
        verify(hallRepository).findAll(pageable);
        verify(hallMapper).toPreviewDto(page);
    }

    @Test
    void update_ShouldReturnPreviewDto_WhenExists() {
        Hall hall = new Hall("Test Hall", SoundSystem.DOLBY_ATMOS);
        HallUpdateDto updateDto = new HallUpdateDto("Updated Hall", SoundSystem.DOLBY_ATMOS);
        HallPreviewDto previewDto = new HallPreviewDto(1L, "Updated Hall", SoundSystem.DOLBY_ATMOS);
        when(hallRepository.findById(1L)).thenReturn(Optional.of(hall));
        when(hallMapper.toPreviewDto(hall)).thenReturn(previewDto);
        HallPreviewDto result = hallService.update(1L, updateDto);
        assertThat(result).isEqualTo(previewDto);
        verify(hallRepository).findById(1L);
        verify(hallMapper).toPreviewDto(hall);
    }

    @Test
    void update_ShouldThrowException_WhenNotExists() {
        HallUpdateDto updateDto = new HallUpdateDto("Updated Hall", SoundSystem.DOLBY_ATMOS);
        when(hallRepository.findById(1L)).thenReturn(Optional.empty());
        org.assertj.core.api.Assertions.assertThatThrownBy(() -> hallService.update(1L, updateDto))
                .isInstanceOf(RuntimeException.class);
        verify(hallRepository).findById(1L);
    }

    @Test
    void delete_ShouldCallRepositoryDelete() {
        hallService.delete(1L);
        verify(hallRepository).deleteById(1L);
    }
} 