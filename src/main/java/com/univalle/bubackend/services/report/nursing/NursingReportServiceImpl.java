package com.univalle.bubackend.services.report.nursing;

import com.univalle.bubackend.DTOs.nursing.NursingReportRequest;
import com.univalle.bubackend.DTOs.nursing.NursingReportResponse;
import com.univalle.bubackend.exceptions.report.ReportNotFound;
import com.univalle.bubackend.models.Diagnostic;
import com.univalle.bubackend.models.NursingActivityLog;
import com.univalle.bubackend.models.NursingReport;
import com.univalle.bubackend.repository.NursingActivityRepository;
import com.univalle.bubackend.repository.ReportNursingRepository;
import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
                .orElseThrow(() -> new ReportNotFound("Informe de enfermeria no encontrado"));

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

    @Override
    public void deleteNursingReport(Integer id) {
        if (!reportNursingRepository.existsById(id)){
            throw new ReportNotFound("Informe de enfermeria no encontrado");
        }
        reportNursingRepository.deleteById(id);
    }

    @Override
    public List<NursingReport> findNursingReports(Integer year, Integer trimester) {
        return reportNursingRepository.findByYearAndTrimester(year, trimester);
    }

    @Override
    public ByteArrayInputStream downloadNursingReport(Integer id) {
        NursingReport nursingReport = reportNursingRepository.findById(id)
                .orElseThrow(() -> new ReportNotFound("Informe de enfermeria no encontrado"));

        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            XSSFSheet sheet = workbook.createSheet("Informe de enfermeria");

            // informacion del informe
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Fecha");
            headerRow.createCell(1).setCellValue(nursingReport.getDate().toString());
            headerRow.createCell(2).setCellValue("Informe");
            headerRow.createCell(3).setCellValue(nursingReport.getYear() + "-" + nursingReport.getTrimester());

            // informacion de las actividades de enfermeria
            Row reportHeader = sheet.createRow(2);
            reportHeader.createCell(0).setCellValue("Motivo");
            reportHeader.createCell(1).setCellValue("Cantidad");

            Map<Diagnostic, Integer> diagnosticCounts = nursingReport.getDiagnosticCounts();

            int rowNum = 3;
            for (Map.Entry<Diagnostic, Integer> entry : diagnosticCounts.entrySet()) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(entry.getKey().toString());
                row.createCell(1).setCellValue(entry.getValue());
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());

        } catch (IOException e) {
            throw new RuntimeException("Error al generar el archivo Excel", e);
        }

    }

    @Override
    public Page<NursingReportResponse> listNursingReports(Pageable pageable) {

        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
                Sort.by(Sort.Direction.DESC, "id"));

        Page<NursingReport> nursingReports = reportNursingRepository.findAll(sortedPageable);

        return nursingReports.map(nursingReport -> NursingReportResponse.builder()
                .date(nursingReport.getDate())
                .year(nursingReport.getYear())
                .trimester(nursingReport.getTrimester())
                .id(nursingReport.getId())
                .build()
        );
    }

}
