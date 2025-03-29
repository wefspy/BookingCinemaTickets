package ru.alexandr.BookingCinemaTickets.domain;

import ru.alexandr.BookingCinemaTickets.domain.enums.SessionSeatStatus;

import java.util.Objects;

public class SessionSeat {
    private Long id;
    private Long sessionId;
    private Long seatId;
    private Double price;
    private SessionSeatStatus status;

    public SessionSeat(Long id,
                       Long sessionId,
                       Long seatId,
                       Double price,
                       SessionSeatStatus status) {
        this.id = id;
        this.sessionId = sessionId;
        this.seatId = seatId;
        this.price = price;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    public Long getSeatId() {
        return seatId;
    }

    public void setSeatId(Long seatId) {
        this.seatId = seatId;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public SessionSeatStatus getStatus() {
        return status;
    }

    public void setStatus(SessionSeatStatus status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SessionSeat that = (SessionSeat) o;

        return Objects.equals(getId(), that.getId())
                && Objects.equals(getSessionId(), that.getSessionId())
                && Objects.equals(getSeatId(), that.getSeatId())
                && Objects.equals(getPrice(), that.getPrice())
                && getStatus() == that.getStatus();
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                getId(),
                getSessionId(),
                getSeatId(),
                getPrice(),
                getStatus());
    }

    @Override
    public String toString() {
        return "SessionSeat{" +
                "id=" + id +
                ", sessionId=" + sessionId +
                ", seatId=" + seatId +
                ", price=" + price +
                ", status=" + status +
                '}';
    }
}
