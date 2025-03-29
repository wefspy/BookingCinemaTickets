package ru.alexandr.BookingCinemaTickets.domain;

import java.time.LocalDateTime;
import java.util.Objects;

public class Session {
    private Long id;
    private Long movieId;
    private Long hallId;
    private LocalDateTime startTime;

    public Session(Long id,
                   Long movieId,
                   Long hallId,
                   LocalDateTime startTime) {
        this.id = id;
        this.movieId = movieId;
        this.hallId = hallId;
        this.startTime = startTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMovieId() {
        return movieId;
    }

    public void setMovieId(Long movieId) {
        this.movieId = movieId;
    }

    public Long getHallId() {
        return hallId;
    }

    public void setHallId(Long hallId) {
        this.hallId = hallId;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Session session = (Session) o;

        return Objects.equals(getId(), session.getId())
                && Objects.equals(getMovieId(), session.getMovieId())
                && Objects.equals(getHallId(), session.getHallId())
                && Objects.equals(getStartTime(), session.getStartTime());
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                getId(),
                getMovieId(),
                getHallId(),
                getStartTime());
    }

    @Override
    public String toString() {
        return "Session{" +
                "id=" + id +
                ", movieId=" + movieId +
                ", hallId=" + hallId +
                ", startTime=" + startTime +
                '}';
    }
}
