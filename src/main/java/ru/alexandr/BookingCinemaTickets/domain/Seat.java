package ru.alexandr.BookingCinemaTickets.domain;

import ru.alexandr.BookingCinemaTickets.domain.enums.SeatType;

import java.time.LocalDateTime;
import java.util.Objects;

public class Seat {
    private Long id;
    private Long hallId;
    private Integer rowNumber;
    private Integer seatNumber;
    private SeatType type;
    private Long transactionId;
    private LocalDateTime paymentTime;

    public Seat(Long id,
                Long hallId,
                Integer rowNumber,
                Integer seatNumber,
                SeatType type,
                Long transactionId,
                LocalDateTime paymentTime) {
        this.id = id;
        this.hallId = hallId;
        this.rowNumber = rowNumber;
        this.seatNumber = seatNumber;
        this.type = type;
        this.transactionId = transactionId;
        this.paymentTime = paymentTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getHallId() {
        return hallId;
    }

    public void setHallId(Long hallId) {
        this.hallId = hallId;
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

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public LocalDateTime getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(LocalDateTime paymentTime) {
        this.paymentTime = paymentTime;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Seat seat = (Seat) o;
        return Objects.equals(getId(), seat.getId())
                && Objects.equals(getHallId(), seat.getHallId())
                && Objects.equals(getRowNumber(), seat.getRowNumber())
                && Objects.equals(getSeatNumber(), seat.getSeatNumber())
                && getType() == seat.getType()
                && Objects.equals(getTransactionId(), seat.getTransactionId())
                && Objects.equals(getPaymentTime(), seat.getPaymentTime());
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                getId(),
                getHallId(),
                getRowNumber(),
                getSeatNumber(),
                getType(),
                getTransactionId(),
                getPaymentTime());
    }

    @Override
    public String toString() {
        return "Seat{" +
                "id=" + id +
                ", hallId=" + hallId +
                ", rowNumber=" + rowNumber +
                ", seatNumber=" + seatNumber +
                ", type=" + type +
                ", transactionId=" + transactionId +
                ", paymentTime=" + paymentTime +
                '}';
    }
}
