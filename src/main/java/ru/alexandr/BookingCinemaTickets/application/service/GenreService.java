package ru.alexandr.BookingCinemaTickets.application.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.alexandr.BookingCinemaTickets.application.dto.genre.GenreCreateDto;
import ru.alexandr.BookingCinemaTickets.application.dto.genre.GenreDto;
import ru.alexandr.BookingCinemaTickets.application.exception.GenreNotFoundException;
import ru.alexandr.BookingCinemaTickets.application.mapper.GenreMapper;
import ru.alexandr.BookingCinemaTickets.domain.model.Genre;
import ru.alexandr.BookingCinemaTickets.infrastructure.repository.jpa.GenreRepository;

@Service
public class GenreService {
    private final GenreRepository genreRepository;
    private final GenreMapper genreMapper;

    public GenreService(GenreRepository genreRepository, GenreMapper genreMapper) {
        this.genreRepository = genreRepository;
        this.genreMapper = genreMapper;
    }

    @Transactional
    public GenreDto create(GenreCreateDto data) {
        Genre entity = genreMapper.toEntity(data);
        entity = genreRepository.save(entity);
        return genreMapper.toDto(entity);
    }

    @Transactional(readOnly = true)
    public Page<GenreDto> getAll(Pageable pageable) {
        Page<Genre> entities = genreRepository.findAll(pageable);
        return genreMapper.toDto(entities);
    }

    @Transactional
    public GenreDto update(Long id, GenreCreateDto data) {
        Genre existGenre = genreRepository.findById(id)
                .orElseThrow(() -> new GenreNotFoundException(String.format("Жанр с id: %s не найден", id)));
        existGenre.update(data);
        return genreMapper.toDto(existGenre);
    }

    @Transactional
    public void delete(Long id) {
        genreRepository.deleteById(id);
    }
}
