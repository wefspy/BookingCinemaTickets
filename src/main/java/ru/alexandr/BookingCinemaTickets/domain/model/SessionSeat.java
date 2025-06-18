package ru.alexandr.BookingCinemaTickets.domain.model;

import jakarta.persistence.*;
import ru.alexandr.BookingCinemaTickets.domain.enums.SessionSeatStatus;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "session_seats")
public class SessionSeat {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sessionSeatsSessionSeatIdSeq")
    @SequenceGenerator(
            name = "sessionSeatsSessionSeatIdSeq",
            sequenceName = "session_seats_session_seat_id_seq",
            allocationSize = 1)
    @Column(name = "session_seat_id")
    private Long id;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private SessionSeatStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    private Session session;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seat_id", nullable = false)
    private Seat seat;

    @OneToOne(mappedBy = "sessionSeat", cascade = CascadeType.ALL, orphanRemoval = true)
    private Ticket ticket;

    public SessionSeat(Session session,
                       Seat seat,
                       BigDecimal price,
                       SessionSeatStatus status) {
        setSession(session);
        setSeat(seat);
        setPrice(price);
        setStatus(status);
    }

    protected SessionSeat() {

    }

    public Long getId() {
        return id;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public Seat getSeat() {
        return seat;
    }

    public void setSeat(Seat seat) {
        this.seat = seat;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public SessionSeatStatus getStatus() {
        return status;
    }

    public void setStatus(SessionSeatStatus status) {
        this.status = status;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        if (this.ticket == ticket) {
            return;
        }

        if (this.ticket != null) {
            this.ticket.setSessionSeat(null);
        }

        this.ticket = ticket;

        if (ticket != null && ticket.getSessionSeat() != this) {
            ticket.setSessionSeat(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SessionSeat that = (SessionSeat) o;

        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "SessionSeat{" +
                "id=" + id +
                ", price=" + price +
                ", status=" + status +
                '}';
    }
}
