package com.univalle.bubackend.services.report.nursing;

import com.univalle.bubackend.DTOs.nursing.NursingReportRequest;
import com.univalle.bubackend.DTOs.nursing.NursingReportResponse;
import com.univalle.bubackend.exceptions.report.InvalidDateFormat;
import com.univalle.bubackend.exceptions.report.ReportNotFound;
import com.univalle.bubackend.models.Diagnostic;
import com.univalle.bubackend.models.NursingActivityLog;
import com.univalle.bubackend.models.NursingReport;
import com.univalle.bubackend.models.NursingReportDetail;
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
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class NursingReportServiceImpl implements INursingReportService {

    private NursingActivityRepository nursingActivityRepository;
    private ReportNursingRepository reportNursingRepository;

    @Override
    public NursingReportResponse generateNursingReport(NursingReportRequest request) {

        int trimester = request.trimester();
        if (trimester < 1 || trimester > 4) {
            throw new InvalidDateFormat("El trimestre debe ser un número entero entre 1 y 4");
        }

        LocalDate startDate = LocalDate.of(request.year(), (trimester - 1) * 3 + 1, 1);
        LocalDate endDate = startDate.plusMonths(3).minusDays(1);

        List<NursingActivityLog> activitiesTrimester = nursingActivityRepository
                .findAllByDateBetween(startDate.atStartOfDay(), endDate.atTime(LocalTime.MAX));

        int totalActivities = activitiesTrimester.size();

        NursingReport report = new NursingReport();
        report.setDate(LocalDate.now());
        report.setYear(request.year());
        report.setTrimester(trimester);
        report.setTotalActivities(totalActivities);

        // Contar el número de ocurrencias de cada diagnóstico
        Map<Diagnostic, Integer> diagnosticCountMap = new HashMap<>();
        for (NursingActivityLog activity : activitiesTrimester) {
            diagnosticCountMap.merge(activity.getDiagnostic(), 1, Integer::sum);
        }

        // Convertir el Map en una lista de NursingReportDetail y asignarla al informe
        List<NursingReportDetail> details = diagnosticCountMap.entrySet().stream()
                .map(entry -> {
                    NursingReportDetail detail = new NursingReportDetail();
                    detail.setDiagnostic(entry.getKey());
                    detail.setCount(entry.getValue());
                    detail.setNursingReport(report); // Relacionar con el informe actual
                    return detail;
                })
                .collect(Collectors.toList());

        report.setDiagnosticCount(details);
        report.setActivities(activitiesTrimester);

        // Guardar el informe con sus detalles
        reportNursingRepository.save(report);

        // Convertir los detalles a un Map para el DTO
        Map<Diagnostic, Integer> diagnosticCountMapForResponse = details.stream()
                .collect(Collectors.toMap(NursingReportDetail::getDiagnostic, NursingReportDetail::getCount));

        return new NursingReportResponse(report, diagnosticCountMapForResponse, totalActivities);
    }



    @Override
    public NursingReportResponse getNursingReport(Integer id) {

        NursingReport report = reportNursingRepository.findById(id)
                .orElseThrow(() -> new ReportNotFound("Informe de enfermeria no encontrado"));

        // Convertir la lista de NursingReportDetail a un Map<Diagnostic, Integer>
        Map<Diagnostic, Integer> diagnosticCounts = report.getDiagnosticCount().stream()
                .collect(Collectors.toMap(NursingReportDetail::getDiagnostic, NursingReportDetail::getCount));

        return new NursingReportResponse(report, diagnosticCounts, report.getTotalActivities());
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

            int rowNum = 3;
            for (NursingReportDetail detail : nursingReport.getDiagnosticCount()) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(detail.getDiagnostic().toString());
                row.createCell(1).setCellValue(detail.getCount());
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
