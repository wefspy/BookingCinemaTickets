package ru.alexandr.BookingCinemaTickets.domain;

import ru.alexandr.BookingCinemaTickets.domain.enums.PaymentStatus;

import java.time.LocalDateTime;
import java.util.Objects;

public class Payment {
    private Long id;
    private Long userId;
    private Long ticketId;
    private Double amount;
    private PaymentStatus status;
    private LocalDateTime paymentDate;

    public Payment(Long id,
                   Long userId,
                   Long ticketId,
                   Double amount,
                   PaymentStatus status,
                   LocalDateTime paymentDate) {
        this.id = id;
        this.userId = userId;
        this.ticketId = ticketId;
        this.amount = amount;
        this.status = status;
        this.paymentDate = paymentDate;
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

    public Long getTicketId() {
        return ticketId;
    }

    public void setTicketId(Long ticketId) {
        this.ticketId = ticketId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Payment payment = (Payment) o;

        return Objects.equals(getId(), payment.getId())
                && Objects.equals(getUserId(), payment.getUserId())
                && Objects.equals(getTicketId(), payment.getTicketId())
                && Objects.equals(getAmount(), payment.getAmount())
                && getStatus() == payment.getStatus()
                && Objects.equals(getPaymentDate(), payment.getPaymentDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                getId(),
                getUserId(),
                getTicketId(),
                getAmount(),
                getStatus(),
                getPaymentDate());
    }

    @Override
    public String toString() {
        return "Payment{" +
                "id=" + id +
                ", userId=" + userId +
                ", ticketId=" + ticketId +
                ", amount=" + amount +
                ", status=" + status +
                ", paymentDate=" + paymentDate +
                '}';
    }
}
