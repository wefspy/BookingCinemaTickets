package ru.alexandr.BookingCinemaTickets.domain;

import jakarta.persistence.*;
import ru.alexandr.BookingCinemaTickets.domain.enums.SoundSystem;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "halls")
public class Hall {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "hall_id")
    private UUID id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "sound_system", nullable = false)
    private SoundSystem soundSystem;

    @OneToMany(mappedBy = "hall", cascade = CascadeType.ALL, orphanRemoval = true)
    private final Set<Seat> seats = new HashSet<>();

    @OneToMany(mappedBy = "hall", cascade = CascadeType.ALL, orphanRemoval = true)
    private final Set<Session> sessions = new HashSet<>();

    public Hall(String name,
                SoundSystem soundSystem) {
        this.name = name;
        this.soundSystem = soundSystem;
    }

    protected Hall() {

    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SoundSystem getSoundSystem() {
        return soundSystem;
    }

    public void setSoundSystem(SoundSystem soundSystem) {
        this.soundSystem = soundSystem;
    }

    public Set<Seat> getSeats() {
        return seats;
    }

    public Set<Session> getSessions() {
        return sessions;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Hall hall = (Hall) o;

        return Objects.equals(getId(), hall.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "Hall{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", soundSystem=" + soundSystem +
                '}';
    }
}
