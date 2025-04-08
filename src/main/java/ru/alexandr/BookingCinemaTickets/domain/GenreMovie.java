package ru.alexandr.BookingCinemaTickets.domain;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "genres_movies")
public class GenreMovie {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "genres_movies_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "genre_id", nullable = false)
    private Genre genre;

    public GenreMovie(Movie movie,
                      Genre genre) {
        setMovie(movie);
        setGenre(genre);
    }

    protected GenreMovie() {

    }

    public Long getId() {
        return id;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        if (this.movie != null) {
            this.movie.getGenreMovie().remove(this);
        }

        this.movie = movie;

        if (movie != null) {
            movie.getGenreMovie().add(this);
        }

    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        if (this.genre != null) {
            this.genre.getGenreMovie().remove(this);
        }

        this.genre = genre;

        if (genre != null) {
            genre.getGenreMovie().add(this);
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
