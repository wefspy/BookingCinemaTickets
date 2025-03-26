package ru.alexandr.BookingCinemaTickets.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import ru.alexandr.BookingCinemaTickets.domain.Movie;

import java.util.List;
import java.util.Map;

@Repository
@Scope(value = BeanDefinition.SCOPE_SINGLETON)
public class MovieRepository implements CrudRepository<Movie, Long> {
    private final Map<Long, Movie> movieContainer;
    private final IdGenerator idGenerator;

    public MovieRepository(@Autowired Map<Long, Movie> movieContainer,
                           @Autowired IdGenerator idGenerator) {
        this.movieContainer = movieContainer;
        this.idGenerator = idGenerator;
    }

    @Override
    public void create(Movie entity) {
        entity.setId(idGenerator.get());
        movieContainer.put(entity.getId(), entity);
    }

    @Override
    public Movie read(Long id) {
        return movieContainer.get(id);
    }

    @Override
    public List<Movie> readAll() {
        return movieContainer.values()
                .stream()
                .toList();
    }

    @Override
    public void update(Movie entity) {
        movieContainer.put(entity.getId(), entity);
    }

    @Override
    public void delete(Long id) {
        movieContainer.remove(id);
    }
}
