package ru.alexandr.BookingCinemaTickets.application.service;

import org.springframework.stereotype.Component;
import ru.alexandr.BookingCinemaTickets.application.exception.ReportNotFoundException;
import ru.alexandr.BookingCinemaTickets.domain.enums.ReportStatus;
import ru.alexandr.BookingCinemaTickets.domain.model.Report;
import ru.alexandr.BookingCinemaTickets.infrastructure.repository.jpa.MovieRepository;
import ru.alexandr.BookingCinemaTickets.infrastructure.repository.jpa.ReportRepository;
import ru.alexandr.BookingCinemaTickets.infrastructure.repository.jpa.UserRepository;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.concurrent.CompletableFuture;

@Component
public class ReportService {
    private final ReportRepository reportRepository;
    private final UserRepository userRepository;
    private final MovieRepository movieRepository;

    public ReportService(ReportRepository reportRepository,
                             UserRepository userRepository,
                             MovieRepository movieRepository) {
        this.reportRepository = reportRepository;
        this.userRepository = userRepository;
        this.movieRepository = movieRepository;
    }

    public Long createReport() {
        Report report = new Report();
        report.setStatus(ReportStatus.CREATED);
        report.setDateTime(LocalDateTime.now());
        reportRepository.save(report);
        return report.getId();
    }

    public Report getReport(Long reportId) {
        return reportRepository.findById(reportId)
                .orElseThrow(() -> new ReportNotFoundException(
                        String.format("Отчет с id %s не найден", reportId)
                ));
    }

    public CompletableFuture<Void> generateReportAsync(Long reportId) {
        return CompletableFuture.runAsync(() -> {
            Report report = getReport(reportId);
            report.setStatus(ReportStatus.IN_PROGRESS);
            reportRepository.save(report);

            try {
                Long startTotal = System.nanoTime();

                Thread userThread = new Thread(() -> {
                    Long start = System.nanoTime();
                    Long userCount = userRepository.count();
                    Long end = System.nanoTime();

                    report.setNumberUsers(userCount);
                    report.setUsersCalculationTime(LocalTime.ofNanoOfDay(end - start));
                });

                Thread movieThread = new Thread(() -> {
                    Long start = System.nanoTime();
                    Long movieCount = movieRepository.count();
                    Long end = System.nanoTime();

                    report.setNumberMovies(movieCount);
                    report.setMoviesCalculationTime(LocalTime.ofNanoOfDay(end - start));
                });

                userThread.start();
                movieThread.start();

                userThread.join();
                movieThread.join();

                report.setStatus(ReportStatus.FINISHED);
                Long endTotal = System.nanoTime();
                report.setCalculateReportTime(LocalTime.ofNanoOfDay(endTotal - startTotal));

                report.setDateTime(LocalDateTime.now());
            } catch (Exception e) {
                report.setStatus(ReportStatus.ERROR);
            }

            reportRepository.save(report);
        });
    }
}
