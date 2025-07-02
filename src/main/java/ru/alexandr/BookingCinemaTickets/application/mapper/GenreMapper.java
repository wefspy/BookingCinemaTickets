package ru.alexandr.BookingCinemaTickets.application.mapper;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import ru.alexandr.BookingCinemaTickets.application.dto.genre.GenreData;
import ru.alexandr.BookingCinemaTickets.application.dto.genre.GenreDto;
import ru.alexandr.BookingCinemaTickets.domain.model.Genre;

@Component
public class GenreMapper {

    public Genre toEntity(GenreData data) {
        return new Genre(data.name(), data.description());
    }

    public GenreDto toDto(Genre genre) {
        return new GenreDto(genre.getId(), genre.getName(), genre.getDescription());
    }

    public Page<GenreDto> toDto(Page<Genre> genres) {
        return genres.map(this::toDto);
    }
}
