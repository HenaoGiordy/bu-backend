package com.univalle.bubackend.services.report;

import com.univalle.bubackend.DTOs.report.UserDTO;
import com.univalle.bubackend.models.Report;
import com.univalle.bubackend.repository.ReportRepository;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.List;

@Service
@AllArgsConstructor
public class ScheduledReportService {
    private final ReportServiceImpl reportServiceImpl;
    private final ReportRepository reportRepository;

    @Scheduled(cron = "0 * * * * ?")
    public void report() {
        LocalDate today = LocalDate.now();
        List<UserDTO> users = reportServiceImpl.generateReport();

        Report todayReport = reportRepository.findByDate(today)
                .orElseGet(() -> reportRepository.save(new Report(null, "Reporte", "almuerzo", today, "2024", new LinkedHashSet<>())));

        String reportName = reportServiceImpl.generateReportName(todayReport);
        LocalDate reportDate = reportServiceImpl.getReportDate();

        System.out.println("Reporte generado: " + reportName);
        System.out.println("Fecha del reporte: " + reportDate);
        users.forEach(user -> {
            System.out.println("Usuario: " + user.name() + " " + user.lastName());
        });

    }

}
