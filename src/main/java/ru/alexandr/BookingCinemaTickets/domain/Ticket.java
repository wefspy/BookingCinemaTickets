package ru.alexandr.BookingCinemaTickets.domain;

import java.time.LocalDateTime;
import java.util.Objects;

public class Ticket {
    private Long id;
    private Long userId;
    private Long sessionSeatId;
    private LocalDateTime bookingTime;
    private String qrCode;
    private Boolean used;

    public Ticket(Long id,
                  Long userId,
                  Long sessionSeatId,
                  LocalDateTime bookingTime,
                  String qrCode,
                  Boolean used) {
        this.id = id;
        this.userId = userId;
        this.sessionSeatId = sessionSeatId;
        this.bookingTime = bookingTime;
        this.qrCode = qrCode;
        this.used = used;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getSessionSeatId() {
        return sessionSeatId;
    }

    public void setSessionSeatId(Long sessionSeatId) {
        this.sessionSeatId = sessionSeatId;
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Ticket ticket = (Ticket) o;
        return Objects.equals(getId(), ticket.getId())
                && Objects.equals(getUserId(), ticket.getUserId())
                && Objects.equals(getSessionSeatId(), ticket.getSessionSeatId())
                && Objects.equals(getBookingTime(), ticket.getBookingTime())
                && Objects.equals(getQrCode(), ticket.getQrCode())
                && Objects.equals(getUsed(), ticket.getUsed());
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                getId(),
                getUserId(),
                getSessionSeatId(),
                getBookingTime(),
                getQrCode(),
                getUsed());
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "id=" + id +
                ", userId=" + userId +
                ", sessionSeatId=" + sessionSeatId +
                ", bookingTime=" + bookingTime +
                ", qrCode='" + qrCode + '\'' +
                ", used=" + used +
                '}';
    }
}
