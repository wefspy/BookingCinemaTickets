package ru.alexandr.BookingCinemaTickets.domain.model;

import jakarta.persistence.*;
import ru.alexandr.BookingCinemaTickets.application.dto.session.SessionData;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "sessions")
public class Session {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sessionsSessionIdSeq")
    @SequenceGenerator(name = "sessionsSessionIdSeq", sequenceName = "sessions_session_id_seq", allocationSize = 1)
    @Column(name = "session_id")
    private Long id;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hall_id", nullable = false)
    private Hall hall;

    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<SessionSeat> sessionSeat = new ArrayList<>();

    public Session(Movie movie,
                   Hall hall,
                   LocalDateTime startTime) {
        setMovie(movie);
        setHall(hall);
        setStartTime(startTime);
    }

    protected Session() {

    }

    public Long getId() {
        return id;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public Hall getHall() {
        return hall;
    }

    public void setHall(Hall hall) {
        this.hall = hall;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public List<SessionSeat> getSessionSeat() {
        return sessionSeat;
    }

    public void update(SessionData data) {
        setStartTime(data.startTime());
    }

    public void updateDependencies(Hall hall, Movie movie) {
        setHall(hall);
        setMovie(movie);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Session session = (Session) o;

        return Objects.equals(getId(), session.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "Session{" +
                "id=" + id +
                ", startTime=" + startTime +
                '}';
    }
}
