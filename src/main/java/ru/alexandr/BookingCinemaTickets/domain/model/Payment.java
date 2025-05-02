package ru.alexandr.BookingCinemaTickets.domain.model;

import jakarta.persistence.*;
import ru.alexandr.BookingCinemaTickets.domain.enums.PaymentStatus;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "payments")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "payment_id")
    private Long id;

    @Column(name = "amount", nullable = false)
    private Double amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private PaymentStatus status;

    @Column(name = "payment_date", nullable = false)
    private LocalDateTime paymentDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserInfo userInfo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id", nullable = false)
    private Ticket ticket;

    public Payment(UserInfo userInfo,
                   Ticket ticket,
                   Double amount,
                   PaymentStatus status,
                   LocalDateTime paymentDate) {
        setUserInfo(userInfo);
        setTicket(ticket);
        setAmount(amount);
        setStatus(status);
        setPaymentDate(paymentDate);
    }

    protected Payment() {

    }

    public Long getId() {
        return id;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        if (this.userInfo != null) {
            this.userInfo.getPayments().remove(this);
        }

        this.userInfo = userInfo;

        if (userInfo != null) {
            userInfo.getPayments().add(this);
        }
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        if (this.ticket != null) {
            this.ticket.getPayments().remove(this);
        }

        this.ticket = ticket;

        if (ticket != null) {
            ticket.getPayments().add(this);
        }
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

        return Objects.equals(getId(), payment.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "Payment{" +
                "id=" + id +
                ", amount=" + amount +
                ", status=" + status +
                ", paymentDate=" + paymentDate +
                '}';
    }
}
