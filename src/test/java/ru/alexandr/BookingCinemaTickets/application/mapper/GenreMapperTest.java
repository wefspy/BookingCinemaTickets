package ru.alexandr.BookingCinemaTickets.application.mapper;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import ru.alexandr.BookingCinemaTickets.application.dto.genre.GenreDto;
import ru.alexandr.BookingCinemaTickets.domain.model.Genre;
import ru.alexandr.BookingCinemaTickets.testUtils.factory.TestEntityFactory;

import java.util.List;

class GenreMapperTest {
    private final GenreMapper genreMapper = new GenreMapper();
    private final GenreDto genreDto = new GenreDto(1L, "GenreName", "description");
    private Genre genre;

    @BeforeEach
    void setUp() {
        genre = TestEntityFactory.genre(genreDto.id(), genreDto.name(), genreDto.description());
    }

    @Test
    void toEntity() {
        Genre actualGenre = genreMapper.toEntity(genreDto);

        Assertions.assertThat(actualGenre).isNotNull();
        Assertions.assertThat(actualGenre.getName()).isEqualTo(genreDto.name());
        Assertions.assertThat(actualGenre.getDescription()).isEqualTo(genreDto.description());
    }

    @Test
    void toDto_WhenGiveGenre() {
        GenreDto actualGenreDto = genreMapper.toDto(genre);

        Assertions.assertThat(actualGenreDto).isNotNull();
        Assertions.assertThat(actualGenreDto.id()).isEqualTo(genre.getId());
        Assertions.assertThat(actualGenreDto.name()).isEqualTo(genre.getName());
        Assertions.assertThat(actualGenreDto.description()).isEqualTo(genre.getDescription());
    }

    @Test
    void toDto_WhenGivenPageGenre() {
        Page<Genre> genrePage = new PageImpl<>(List.of(genre));

        Page<GenreDto> result = genreMapper.toDto(genrePage);

        Assertions.assertThat(genrePage).hasSize(result.getSize());
    }
}