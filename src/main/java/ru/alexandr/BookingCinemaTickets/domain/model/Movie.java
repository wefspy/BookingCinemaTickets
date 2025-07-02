package ru.alexandr.BookingCinemaTickets.domain.model;

import jakarta.persistence.*;
import ru.alexandr.BookingCinemaTickets.application.dto.movie.MovieData;
import ru.alexandr.BookingCinemaTickets.domain.enums.Rating;

import java.net.URL;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(name = "movies")
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "moviesMovieIdSeq")
    @SequenceGenerator(name = "moviesMovieIdSeq", sequenceName = "movies_movie_id_seq", allocationSize = 1)
    @Column(name = "movie_id")
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "duration_in_minutes", nullable = false)
    private Integer durationInMinutes;

    @Column(name = "release_date", nullable = false)
    private LocalDate releaseDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "rating", nullable = false)
    private Rating rating;

    @Column(name = "poster_url")
    private URL posterUrl;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<GenreMovie> genreMovie = new ArrayList<>();

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Session> sessions = new ArrayList<>();

    public Movie(String title,
                 String description,
                 Integer durationInMinutes,
                 LocalDate releaseDate,
                 Rating rating) {
        setTitle(title);
        setDescription(description);
        setDurationInMinutes(durationInMinutes);
        setReleaseDate(releaseDate);
        setRating(rating);
    }

    protected Movie() {

    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getDurationInMinutes() {
        return durationInMinutes;
    }

    public void setDurationInMinutes(Integer durationInMinutes) {
        this.durationInMinutes = durationInMinutes;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Rating getRating() {
        return rating;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }

    public URL getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(URL posterUrl) {
        this.posterUrl = posterUrl;
    }

    public List<GenreMovie> getGenreMovie() {
        return genreMovie;
    }

    public List<Session> getSessions() {
        return sessions;
    }

    public void update(MovieData data) {
        setTitle(data.title());
        setDescription(data.description());
        setDurationInMinutes(data.durationInMinutes());
        setReleaseDate(data.releaseDate());
        setRating(data.rating());
    }

    public void addGenres(List<Genre> newGenres) {
        Set<Genre> currentGenres = getGenreMovie().stream()
                .map(GenreMovie::getGenre)
                .collect(Collectors.toSet());

        getGenreMovie().removeIf(gm -> !newGenres.contains(gm.getGenre()));

        for (Genre genre : newGenres) {
            if (currentGenres.contains(genre)) {
                continue;
            }
            getGenreMovie().add(new GenreMovie(this, genre));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Movie movie = (Movie) o;

        return Objects.equals(getId(), movie.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", durationInMinutes='" + durationInMinutes + '\'' +
                ", releaseDate=" + releaseDate +
                ", rating=" + rating +
                ", posterUrl=" + posterUrl +
                '}';
    }
}
