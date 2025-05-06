package ru.alexandr.BookingCinemaTickets.domain.model;

import jakarta.persistence.*;
import ru.alexandr.BookingCinemaTickets.domain.enums.ReportStatus;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

@Entity
@Table(name = "service_reports")
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "service_report_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ReportStatus status;

    @Column(name = "dateTime")
    private LocalDateTime dateTime;

    @Column(name = "calculate_report_time")
    private LocalTime calculateReportTime;

    @Column(name = "number_users")
    private Long numberUsers;

    @Column(name = "users_calculation_time")
    private LocalTime usersCalculationTime;

    @Column(name = "number_movies")
    private Long numberMovies;

    @Column(name = "movies_calculation_time")
    private LocalTime moviesCalculationTime;

    public Report() {}

    public Long getId() {
        return id;
    }

    public ReportStatus getStatus() {
        return status;
    }

    public void setStatus(ReportStatus status) {
        this.status = status;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public LocalTime getCalculateReportTime() {
        return calculateReportTime;
    }

    public void setCalculateReportTime(LocalTime calculateReportTime) {
        this.calculateReportTime = calculateReportTime;
    }

    public Long getNumberUsers() {
        return numberUsers;
    }

    public void setNumberUsers(Long numberUsers) {
        this.numberUsers = numberUsers;
    }

    public LocalTime getUsersCalculationTime() {
        return usersCalculationTime;
    }

    public void setUsersCalculationTime(LocalTime usersCalculationTime) {
        this.usersCalculationTime = usersCalculationTime;
    }

    public Long getNumberMovies() {
        return numberMovies;
    }

    public void setNumberMovies(Long numberMovies) {
        this.numberMovies = numberMovies;
    }

    public LocalTime getMoviesCalculationTime() {
        return moviesCalculationTime;
    }

    public void setMoviesCalculationTime(LocalTime moviesCalculationTime) {
        this.moviesCalculationTime = moviesCalculationTime;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Report report = (Report) o;

        return Objects.equals(getId(), report.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Report{" +
                "id=" + id +
                ", status=" + status +
                ", dateTime=" + dateTime +
                ", calculateReportTime=" + calculateReportTime +
                ", numberUsers=" + numberUsers +
                ", usersCalculationTime=" + usersCalculationTime +
                ", numberMovies=" + numberMovies +
                ", moviesCalculationTime=" + moviesCalculationTime +
                '}';
    }
}
