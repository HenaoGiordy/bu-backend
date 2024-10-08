package com.univalle.bubackend.services.report;

import com.univalle.bubackend.DTOs.report.ReportRequest;
import com.univalle.bubackend.DTOs.report.ReportResponse;
import com.univalle.bubackend.DTOs.report.UserDTO;
import com.univalle.bubackend.exceptions.change_password.PasswordError;
import com.univalle.bubackend.exceptions.report.BecaInvalid;
import com.univalle.bubackend.exceptions.report.ReportNotFound;
import com.univalle.bubackend.models.Report;
import com.univalle.bubackend.models.UserEntity;
import com.univalle.bubackend.models.Reservation;
import com.univalle.bubackend.repository.ReportRepository;
import com.univalle.bubackend.repository.UserEntityRepository;
import lombok.AllArgsConstructor;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.springframework.stereotype.Service;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ReportServiceImpl {
    private final UserEntityRepository userEntityRepository;
    private final ReportRepository reportRepository;

    public Report generateReport(ReportRequest reportRequest) {
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(LocalTime.MAX);

        List<UserEntity> filterUsers;

        if ("almuerzo".equalsIgnoreCase(reportRequest.beca())) {
            filterUsers = userEntityRepository.findUserLunchPaid(startOfDay, endOfDay);
        } else if ("refrigerio".equalsIgnoreCase(reportRequest.beca())) {
            filterUsers = userEntityRepository.findUserSnackPaid(startOfDay, endOfDay);
        } else {
            throw new BecaInvalid("Tipo de beca no valida");
        }

        Report report = new Report();
        report.setDate(today);
        report.setBeca(reportRequest.beca());
        report.setSemester(reportRequest.semester());
        report.setUserEntities(new HashSet<>(filterUsers));

        Map<Integer, Integer> countReports = new HashMap<>();
        for (UserEntity user : filterUsers) {

            long count = user.getReports().stream()
                    .filter(r -> r.getBeca().equalsIgnoreCase("almuerzo") && reportRequest.beca().equalsIgnoreCase("almuerzo"))
                    .count() + user.getReports().stream()
                    .filter(r -> r.getBeca().equalsIgnoreCase("refrigerio") && reportRequest.beca().equalsIgnoreCase("refrigerio"))
                    .count();

            countReports.put(user.getId(), (int) count);

        }
        report.setUserReportCount(countReports);

        report = reportRepository.save(report);

        for (UserEntity user: filterUsers) {
            user.getReports().add(report);
            userEntityRepository.save(user);
        }

        return report;

    }

    public void deleteReport(Integer id) {
        if (!reportRepository.existsById(id)) {
            throw new ReportNotFound("Informe no encontrado");
        }
        reportRepository.deleteById(id);
    }

    public ByteArrayInputStream generateExcelReport(Integer id) {
        Report report = reportRepository.findById(id)
                .orElseThrow(() -> new ReportNotFound("Informe no encontrado"));

        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            XSSFSheet sheet = workbook.createSheet("Reporte");

            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Fecha");
            headerRow.createCell(1).setCellValue(report.getDate().toString());
            headerRow.createCell(2).setCellValue("Tipo de beca");
            headerRow.createCell(3).setCellValue(report.getBeca());

            Row userHeader = sheet.createRow(2);
            userHeader.createCell(0).setCellValue("Codigo/Cedula");
            userHeader.createCell(1).setCellValue("Nombre");
            userHeader.createCell(2).setCellValue("Plan/Area");
            userHeader.createCell(3).setCellValue("Correo");

            int countCellIndex = 4;
            if (report.getSemester() != null) {
                userHeader.createCell(countCellIndex).setCellValue("Cantidad de " + report.getBeca());
            }

            int rowNum = 3;
            for (UserEntity user : report.getUserEntities()) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(user.getUsername());
                row.createCell(1).setCellValue(user.getName() + " " + user.getLastName());
                row.createCell(2).setCellValue(user.getPlan());
                row.createCell(3).setCellValue(user.getEmail());

                if (report.getSemester() != null) {
                    int count = report.getUserReportCount().getOrDefault(user.getId(), 0);
                    row.createCell(countCellIndex).setCellValue(count);
                }

            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());

        } catch (IOException e) {
            throw new RuntimeException("Error al generar el archivo Excel", e);
        }
    }

    public List<Report> findReportsBySemester(String semester) {
        return reportRepository.findBySemester(semester);
    }

    public List<Report> findReportsByDate(LocalDate date) {
        return reportRepository.findAllByDate(date);
    }

    public ReportResponse viewReport(Integer id){
        Report report = reportRepository.findById(id)
                .orElseThrow(() -> new ReportNotFound("Informe no encontrado"));

        List<UserDTO> users = report.getUserEntities().stream()
                .map(user -> {
                    int count = report.getUserReportCount().getOrDefault(user.getId(), 0);

                    return new UserDTO(
                            user.getUsername(),
                            user.getName() + " " + user.getLastName(),
                            user.getEmail(),
                            user.getPlan(),
                            user.getRoles(),
                            user.getIsActive(),
                            user.getLunchBeneficiary(),
                            user.getSnackBeneficiary(),
                            count
                    );
                })
                .collect(Collectors.toList());

        return new ReportResponse(report.getId(), report.getDate(), report.getSemester(), report.getBeca(), users);

    }

    public List<ReportResponse> listReports() {
        List<Report> reports = reportRepository.findAll();
        return reports.stream().map(report -> ReportResponse.builder()
                .beca(report.getBeca())
                .date(report.getDate())
                .id(report.getId())
                .semester(report.getSemester())
                .build()
        ).collect(Collectors.toList());

    }

}
