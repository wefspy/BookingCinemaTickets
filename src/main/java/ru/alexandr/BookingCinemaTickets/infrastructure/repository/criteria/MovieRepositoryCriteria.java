package ru.alexandr.BookingCinemaTickets.infrastructure.repository.criteria;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Repository;
import ru.alexandr.BookingCinemaTickets.domain.model.Movie;
import ru.alexandr.BookingCinemaTickets.domain.repository.MovieRepositoryCustom;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class MovieRepositoryCriteria implements MovieRepositoryCustom {

    private final EntityManager entityManager;

    public MovieRepositoryCriteria(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Movie> findByReleaseDateBetween(LocalDateTime startDate, LocalDateTime endDate) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Movie> criteriaQuery = criteriaBuilder.createQuery(Movie.class);

        Root<Movie> movie = criteriaQuery.from(Movie.class);
        criteriaQuery.select(movie)
                .where(criteriaBuilder.between(movie.get("releaseDate"), startDate, endDate));

        TypedQuery<Movie> query = entityManager.createQuery(criteriaQuery);
        return query.getResultList();
    }
}
