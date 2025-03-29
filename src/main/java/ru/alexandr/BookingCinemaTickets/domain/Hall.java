package ru.alexandr.BookingCinemaTickets.domain;

import ru.alexandr.BookingCinemaTickets.domain.enums.SoundSystem;

import java.util.Objects;

public class Hall {
    private Long id;
    private String name;
    private SoundSystem soundSystem;

    public Hall(Long id,
                String name,
                SoundSystem soundSystem) {
        this.id = id;
        this.name = name;
        this.soundSystem = soundSystem;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Hall hall = (Hall) o;

        return Objects.equals(getId(), hall.getId())
                && Objects.equals(getName(), hall.getName())
                && getSoundSystem() == hall.getSoundSystem();
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                getId(),
                getName(),
                getSoundSystem());
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
