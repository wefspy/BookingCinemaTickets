package ru.alexandr.BookingCinemaTickets.domain;

import java.util.Objects;

public class MovieGenre {
    private final Long id;
    private Long movieId;
    private Long genreId;

    public MovieGenre(Long id, Long movieId, Long genreId) {
        this.id = id;
        this.movieId = movieId;
        this.genreId = genreId;
    }

    public Long getId() {
        return id;
    }

    public Long getMovieId() {
        return movieId;
    }

    public void setMovieId(Long movieId) {
        this.movieId = movieId;
    }

    public Long getGenreId() {
        return genreId;
    }

    public void setGenreId(Long genreId) {
        this.genreId = genreId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MovieGenre that = (MovieGenre) o;
        return Objects.equals(getId(), that.getId())
                && Objects.equals(getMovieId(), that.getMovieId())
                && Objects.equals(getGenreId(), that.getGenreId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(),
                getMovieId(),
                getGenreId());
    }

    @Override
    public String toString() {
        return "MovieGenre{" +
                "id=" + id +
                ", movieId=" + movieId +
                ", genreId=" + genreId +
                '}';
    }
}
