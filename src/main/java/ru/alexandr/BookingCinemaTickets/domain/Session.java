package ru.alexandr.BookingCinemaTickets.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "sessions")
public class Session {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "session_id")
    private UUID id;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hall_id", nullable = false)
    private Hall hall;

    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL, orphanRemoval = true)
    private final Set<SessionSeat> sessionSeat = new HashSet<>();

    public Session(Movie movie,
                   Hall hall,
                   LocalDateTime startTime) {
        this.movie = movie;
        this.hall = hall;
        this.startTime = startTime;
    }

    protected Session() {

    }

    public UUID getId() {
        return id;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        if (movie != null) {
            this.movie.getSessions().remove(this);
        }

        this.movie = movie;

        if (movie != null) {
            movie.getSessions().add(this);
        }
    }

    public Hall getHall() {
        return hall;
    }

    public void setHall(Hall hall) {
        if (this.hall != null) {
            this.hall.getSessions().remove(this);
        }

        this.hall = hall;

        if (hall != null) {
            hall.getSessions().add(this);
        }
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public Set<SessionSeat> getSessionSeat() {
        return sessionSeat;
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
