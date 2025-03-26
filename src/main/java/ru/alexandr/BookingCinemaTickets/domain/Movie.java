package ru.alexandr.BookingCinemaTickets.domain;

import ru.alexandr.BookingCinemaTickets.domain.enums.Rating;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.Objects;

public class Movie {
    private Long id;
    private String title;
    private String description;
    private Integer durationInMinutes;
    private LocalDateTime releaseDate;
    private Rating rating;
    private URL posterUrl;

    public Movie(Long id,
                 String title,
                 String description,
                 Integer durationInMinutes,
                 LocalDateTime releaseDate,
                 Rating rating,
                 URL posterUrl) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.durationInMinutes = durationInMinutes;
        this.releaseDate = releaseDate;
        this.rating = rating;
        this.posterUrl = posterUrl;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Movie movie = (Movie) o;
        return Objects.equals(getId(), movie.getId())
                && Objects.equals(getTitle(), movie.getTitle())
                && Objects.equals(getDescription(), movie.getDescription())
                && Objects.equals(getDurationInMinutes(), movie.getDurationInMinutes())
                && Objects.equals(getReleaseDate(), movie.getReleaseDate())
                && getRating() == movie.getRating()
                && Objects.equals(getPosterUrl(), movie.getPosterUrl());
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                getId(),
                getTitle(),
                getDescription(),
                getDurationInMinutes(),
                getReleaseDate(),
                getRating(),
                getPosterUrl());
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
