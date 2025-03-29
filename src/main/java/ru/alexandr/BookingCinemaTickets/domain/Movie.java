package ru.alexandr.BookingCinemaTickets.domain;

import jakarta.persistence.*;
import ru.alexandr.BookingCinemaTickets.domain.enums.Rating;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "movies")
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "movie_id")
    private UUID id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "duration_in_minutes", nullable = false)
    private Integer durationInMinutes;

    @Column(name = "release_date", nullable = false)
    private LocalDateTime releaseDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "rating", nullable = false)
    private Rating rating;

    @Column(name = "poster_url")
    private URL posterUrl;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true)
    private final Set<GenreMovie> genres = new HashSet<>();

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true)
    private final Set<Session> sessions = new HashSet<>();

    public Movie(String title,
                 String description,
                 Integer durationInMinutes,
                 LocalDateTime releaseDate,
                 Rating rating,
                 URL posterUrl) {
        this.title = title;
        this.description = description;
        this.durationInMinutes = durationInMinutes;
        this.releaseDate = releaseDate;
        this.rating = rating;
        this.posterUrl = posterUrl;
    }

    protected Movie() {

    }

    public UUID getId() {
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

    public LocalDateTime getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDateTime releaseDate) {
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

    public Set<GenreMovie> getGenres() {
        return genres;
    }

    public Set<Session> getSessions() {
        return sessions;
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
