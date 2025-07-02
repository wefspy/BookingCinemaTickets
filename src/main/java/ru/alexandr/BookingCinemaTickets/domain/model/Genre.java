package ru.alexandr.BookingCinemaTickets.domain.model;

import jakarta.persistence.*;
import ru.alexandr.BookingCinemaTickets.application.dto.genre.GenreData;

import java.util.*;

@Entity
@Table(name = "genres")
public class Genre {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "genresGenreIdSeq")
    @SequenceGenerator(name = "genresGenreIdSeq", sequenceName = "genres_genre_id_seq", allocationSize = 1)
    @Column(name = "genre_id")
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "genre", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<GenreMovie> genreMovie = new ArrayList<>();

    public Genre(String name, String description) {
        setName(name);
        setDescription(description);
    }

    protected Genre() {

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<GenreMovie> getGenreMovie() {
        return genreMovie;
    }

    public void update(GenreData data) {
        setName(data.name());
        setDescription(data.description());
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Genre genre = (Genre) o;
        return Objects.equals(getId(), genre.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "Genre{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
