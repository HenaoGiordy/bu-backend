package com.univalle.bubackend.services.report.nursing;

import com.univalle.bubackend.DTOs.nursing.NursingReportRequest;
import com.univalle.bubackend.DTOs.nursing.NursingReportResponse;
import com.univalle.bubackend.exceptions.ResourceNotFoundException;
import com.univalle.bubackend.models.Diagnostic;
import com.univalle.bubackend.models.NursingActivityLog;
import com.univalle.bubackend.models.NursingReport;
import com.univalle.bubackend.repository.NursingActivityRepository;
import com.univalle.bubackend.repository.ReportNursingRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class NursingReportServiceImpl implements INursingReportService {

    private NursingActivityRepository nursingActivityRepository;
    private ReportNursingRepository reportNursingRepository;

    @Override
    public NursingReportResponse generateNursingReport(NursingReportRequest request) {
        // Calcular las fechas de inicio y fin del trimestre basado en el año y trimestre proporcionados
        LocalDate startDate = LocalDate.of(request.year(), (request.trimester() - 1) * 3 + 1, 1);
        LocalDate endDate = startDate.plusMonths(3).minusDays(1);

        List<NursingActivityLog> activitiesTrimester = nursingActivityRepository.
                findAllByDateBetween(startDate.atStartOfDay(), endDate.atTime(LocalTime.MAX));

        Map<Diagnostic, Integer> diagnosticCount = new HashMap<>();
        for (NursingActivityLog activity : activitiesTrimester) {
            diagnosticCount.merge(activity.getDiagnostic(), 1, Integer::sum);
        }

        NursingReport report = new NursingReport();
        report.setDate(LocalDate.now());
        report.setYear(request.year());
        report.setTrimester(request.trimester());
        report.setDiagnosticCounts(diagnosticCount);
        report.setActivities(activitiesTrimester);

        reportNursingRepository.save(report);

        int totalActivities = activitiesTrimester.size();

        return new NursingReportResponse(report, totalActivities);
    }

    @Override
    public NursingReportResponse getNursingReport(Integer id) {

        NursingReport report = reportNursingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Informe de enfermeria no encontrado"));

        Map<Diagnostic, Integer> diagnosticCounts = report.getDiagnosticCounts();

        return new NursingReportResponse(
                report.getId(),
                report.getYear(),
                report.getTrimester(),
                report.getDate(),
                diagnosticCounts,
                report.getActivities().size()
        );
    }
}
