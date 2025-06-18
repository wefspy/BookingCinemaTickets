package ru.alexandr.BookingCinemaTickets.domain.model;

import jakarta.persistence.*;
import ru.alexandr.BookingCinemaTickets.domain.enums.SoundSystem;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "halls")
public class Hall {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "hallsHallIdSeq")
    @SequenceGenerator(name = "hallsHallIdSeq", sequenceName = "halls_hall_id_seq", allocationSize = 1)
    @Column(name = "hall_id")
    private Long id;

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
        setName(name);
        setSoundSystem(soundSystem);
    }

    protected Hall() {

    }

    public Long getId() {
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
