package ru.alexandr.BookingCinemaTickets.domain;

import jakarta.persistence.*;

import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "genres_movies")
public class GenreMovie {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "genres_movies_id")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "genre_id", nullable = false)
    private Genre genre;

    public GenreMovie(Movie movie,
                      Genre genre) {
        this.movie = movie;
        this.genre = genre;
    }

    protected GenreMovie() {

    }

    public UUID getId() {
        return id;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        if (this.movie != null) {
            this.movie.getGenres().remove(this);
        }

        this.movie = movie;

        if (movie != null) {
            movie.getGenres().add(this);
        }

    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        if (this.genre != null) {
            this.genre.getMovies().remove(this);
        }

        this.genre = genre;

        if (genre != null) {
            genre.getMovies().add(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GenreMovie that = (GenreMovie) o;

        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "MovieGenre{" +
                "id=" + id +
                ", movie=" + movie +
                ", genre=" + genre +
                '}';
    }
}
