package com.univalle.bubackend.DTOs.nursing;

import com.univalle.bubackend.models.Diagnostic;
import com.univalle.bubackend.models.NursingReport;

import java.time.LocalDate;
import java.util.Map;

public record NursingReportResponse(
        Integer id,
        int year,
        int trimester,
        LocalDate date,
        Map<Diagnostic, Integer> diagnosticCounts,
        int totalActivities
) {
    public NursingReportResponse(NursingReport report, int totalActivities) {
        this(
                report.getId(),
                report.getYear(),
                report.getTrimester(),
                report.getDate(),
                report.getDiagnosticCounts(),
                totalActivities
        );
    }
}
