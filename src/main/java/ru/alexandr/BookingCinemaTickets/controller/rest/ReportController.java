package ru.alexandr.BookingCinemaTickets.controller.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.alexandr.BookingCinemaTickets.application.service.ReportService;
import ru.alexandr.BookingCinemaTickets.domain.enums.ReportStatus;
import ru.alexandr.BookingCinemaTickets.domain.model.Report;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @PostMapping
    public ResponseEntity<Long> createAndGenerateReport() {
        Long reportId = reportService.createReport();
        reportService.generateReportAsync(reportId);
        return ResponseEntity.ok(reportId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getReport(@PathVariable Long id) {
        Report report = reportService.getReport(id);

        if (report.getStatus() == ReportStatus.CREATED || report.getStatus() == ReportStatus.IN_PROGRESS) {
            return ResponseEntity.ok("Отчет еще формируется. Статус: " + report.getStatus());
        }

        if (report.getStatus() == ReportStatus.ERROR) {
            return ResponseEntity.ok("При формировании отчета произошла ошибка.");
        }

        return ResponseEntity.ok(report);
    }
}

