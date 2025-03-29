package ru.alexandr.BookingCinemaTickets.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "tickets")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "ticket_id")
    private UUID id;

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
        this.userInfo = userInfo;
        this.sessionSeat = sessionSeat;
        this.bookingTime = bookingTime;
        this.qrCode = qrCode;
        this.used = used;
    }

    protected Ticket() {

    }

    public UUID getId() {
        return id;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        if (this.userInfo != null) {
            this.userInfo.getTickets().remove(this);
        }

        this.userInfo = userInfo;

        if (userInfo != null) {
            userInfo.getTickets().add(this);
        }
    }

    public SessionSeat getSessionSeat() {
        return sessionSeat;
    }

    public void setSessionSeat(SessionSeat sessionSeat) {
        if (this.sessionSeat != null) {
            this.sessionSeat.setTicket(null);
        }

        this.sessionSeat = sessionSeat;

        if (sessionSeat != null) {
            sessionSeat.setTicket(this);
        }
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
