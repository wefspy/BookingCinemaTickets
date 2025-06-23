package ru.alexandr.BookingCinemaTickets.domain.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "tickets")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ticketsTicketIdSeq")
    @SequenceGenerator(name = "ticketsTicketIdSeq", sequenceName = "tickets_ticket_id_seq", allocationSize = 1)
    @Column(name = "ticket_id")
    private Long id;

    @Column(name = "booking_time", nullable = false)
    private LocalDateTime bookingTime;

    @Column(name = "qr_code", nullable = false, unique = true)
    private String qrCode;

    @Column(name = "used", nullable = false)
    private Boolean used;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserInfo userInfo;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_seats_id", nullable = false, unique = true)
    private SessionSeat sessionSeat;

    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL, orphanRemoval = true)
    private final Set<Payment> payments = new HashSet<>();

    public Ticket(UserInfo userInfo,
                  SessionSeat sessionSeat,
                  LocalDateTime bookingTime,
                  String qrCode,
                  Boolean used) {
        setUserInfo(userInfo);
        setSessionSeat(sessionSeat);
        setBookingTime(bookingTime);
        setQrCode(qrCode);
        setUsed(used);
    }

    protected Ticket() {

    }

    public Long getId() {
        return id;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public SessionSeat getSessionSeat() {
        return sessionSeat;
    }

    public void setSessionSeat(SessionSeat sessionSeat) {
        this.sessionSeat = sessionSeat;
    }

    public LocalDateTime getBookingTime() {
        return bookingTime;
    }

    public void setBookingTime(LocalDateTime bookingTime) {
        this.bookingTime = bookingTime;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public Boolean getUsed() {
        return used;
    }

    public void setUsed(Boolean used) {
        this.used = used;
    }

    public Set<Payment> getPayments() {
        return payments;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Ticket ticket = (Ticket) o;

        return Objects.equals(getId(), ticket.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "id=" + id +
                ", bookingTime=" + bookingTime +
                ", qrCode='" + qrCode + '\'' +
                ", used=" + used +
                '}';
    }
}
