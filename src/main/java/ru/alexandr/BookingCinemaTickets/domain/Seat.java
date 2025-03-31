package ru.alexandr.BookingCinemaTickets.domain;

import jakarta.persistence.*;
import ru.alexandr.BookingCinemaTickets.domain.enums.SeatType;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "seats")
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "seat_id")
    private UUID id;

    @Column(name = "row_number", nullable = false)
    private Integer rowNumber;

    @Column(name = "seat_number", nullable = false)
    private Integer seatNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private SeatType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hall_id", nullable = false)
    private Hall hall;

    @OneToMany(mappedBy = "seat", cascade = CascadeType.ALL, orphanRemoval = true)
    private final Set<SessionSeat> sessionSeats = new HashSet<>();

    public Seat(Hall hall,
                Integer rowNumber,
                Integer seatNumber,
                SeatType type) {
        setHall(hall);
        setRowNumber(rowNumber);
        setSeatNumber(seatNumber);
        setType(type);
    }

    protected Seat() {

    }

    public UUID getId() {
        return id;
    }

    public Hall getHall() {
        return hall;
    }

    public void setHall(Hall hall) {
        if (this.hall != null) {
            this.hall.getSeats().remove(this);
        }

        this.hall = hall;

        if (hall != null) {
            hall.getSeats().add(this);
        }
    }

    public Integer getRowNumber() {
        return rowNumber;
    }

    public void setRowNumber(Integer rowNumber) {
        this.rowNumber = rowNumber;
    }

    public Integer getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(Integer seatNumber) {
        this.seatNumber = seatNumber;
    }

    public SeatType getType() {
        return type;
    }

    public void setType(SeatType type) {
        this.type = type;
    }

    public Set<SessionSeat> getSessionSeats() {
        return sessionSeats;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Seat seat = (Seat) o;
        return Objects.equals(getId(), seat.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "Seat{" +
                "id=" + id +
                ", rowNumber=" + rowNumber +
                ", seatNumber=" + seatNumber +
                ", type=" + type +
                '}';
    }
}
